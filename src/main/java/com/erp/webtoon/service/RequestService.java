package com.erp.webtoon.service;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.domain.Message;
import com.erp.webtoon.domain.Request;
import com.erp.webtoon.domain.RequestDt;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.itsm.*;
import com.erp.webtoon.dto.message.FeedbackListDto;
import com.erp.webtoon.dto.message.MessageSaveDto;
import com.erp.webtoon.repository.MessageRepository;
import com.erp.webtoon.repository.RequestRepository;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {

     private final UserRepository userRepository;
     private final RequestRepository requestRepository;
     private final MessageRepository messageRepository;
     private final MessageService messageService;
     private final SlackService slackService;
     private final FileService fileService;

    /**
     * 구매 요청 기능
     */
    @Transactional
    public Request purchaseRequest(RequestDto requestDto) throws Exception {

        User reqUser = userRepository.findByEmployeeId(requestDto.getReqUserId()).get();
        User itUser = userRepository.findByEmployeeId(requestDto.getItUserId()).get();
        List<RequestDt> requestDtList = new ArrayList<>();
        List<File> fileList = saveFile(requestDto);

        for(int i = 0; i < requestDto.getRequestDts().size(); i++){
            RequestDt request = RequestDt.builder()
                    .sortSequence(requestDto.getRequestDts().get(i).getSortSequence())
                    .content(requestDto.getRequestDts().get(i).getContent())
                    .count(requestDto.getRequestDts().get(i).getCount())
                    .cost(requestDto.getRequestDts().get(i).getCost())
                    .build();
            requestDtList.add(request);
        }

        Request request = Request.builder()
                .reqType(requestDto.getReqType())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .step(requestDto.getStep())
                .dueDate(requestDto.getDueDate())
                .doneDate(requestDto.getDueDate())
                .reqUser(reqUser)
                .itUser(itUser)
                .files(fileList)
                .requestDts(requestDtList)
                .build();

        requestRepository.save(request);
        return request;
    }

    /**
     * 업무 지원 요청 기능
     */
    @Transactional
    public Request assistRequest(RequestDto requestDto) throws Exception {
        User reqUser = userRepository.findByEmployeeId(requestDto.getReqUserId()).get();
        User itUser = userRepository.findByEmployeeId(requestDto.getItUserId()).get();
        List<File> fileList = saveFile(requestDto);

        Request request = Request.builder()
                .reqType(requestDto.getReqType())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .step(requestDto.getStep())
                .dueDate(requestDto.getDueDate())
                .doneDate(requestDto.getDueDate())
                .reqUser(reqUser)
                .itUser(itUser)
                .files(fileList)
                .build();

        requestRepository.save(request);
        return request;
    }

    /**
     * 요청 조회 기능
     */
    public RequestResponseDto search(Long requestId) {
        Request findRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 서비스 요청입니다."));

        // 상세요청 리스트
        List<RequestDtResponseDto> requestDtList = findRequest.getRequestDts().stream()
                .map(RequestDtResponseDto::new)
                .collect(Collectors.toList());

        // 첨부파일 리스트
        List<String> files = findRequest.getFiles().stream()
                .map(file -> file.getOriginName())
                .collect(Collectors.toList());

        return RequestResponseDto.builder()
                .reqType(findRequest.getReqType())
                .title(findRequest.getTitle())
                .content(findRequest.getContent())
                .step(findRequest.getStep())
                .dueDate(findRequest.getDueDate())
                .doneDate(findRequest.getDoneDate())
                .reqUserId(findRequest.getReqUser().getEmployeeId())
                .itUserId(findRequest.getItUser().getEmployeeId())
                .fileOriginName(files)
                .requestDtList(requestDtList)
                .build();
    }

    /**
     * 사원별 개인 요청 리스트 조회 기능
     */
    public List<RequestListResponseDto> searchUserList(String employeeId) {
        User findUser = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

        return findUser.getRequests().stream()
                .sorted(Comparator.comparing(Request::getStep))
                .map(RequestListResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * IT팀 과거 요청 전체 리스트 조회
     */
    public List<RequestListResponseDto> searchAllList(String employeeId) throws IllegalAccessException {
        User findUser = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

        //IT팀인 경우에만
        if(findUser.getDeptName().contains("IT")) {
            return requestRepository.findAll(Sort.by("step")).stream()
                    .map(RequestListResponseDto::new)
                    .collect(Collectors.toList());
        }
        throw new IllegalAccessException("IT팀이 아닙니다.");
    }

    /**
     * 단계 변경 기능
     */
    @Transactional
    public void changeStep(Long requestId, RequestStepDto dto) {
        Request findRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 서비스 요청입니다."));

        if(dto.getStep() > findRequest.getStep() && dto.getStep() < 6) {
            findRequest.changeStep(dto.getStep());
        }
    }


    /**
     * 코멘트 등록 기능
     */
    @Transactional
    public void registerComment(MessageSaveDto dto) throws IOException{
        User sendUser = userRepository.findByEmployeeId(dto.getSendEmpId())
                .orElseThrow(() -> new EntityNotFoundException("메시지 발신 직원의 정보가 존재하지 않습니다."));

        //피드백 저장
        Message feedbackMsg = dto.toEntity(null, sendUser);
        messageRepository.save(feedbackMsg);

        //메시지 저장
        Request request = requestRepository.findById(feedbackMsg.getRefId())
                .orElseThrow(() -> new EntityNotFoundException("요청 정보가 존재하지 않습니다."));

        String originContent = feedbackMsg.getContent();
        dto.setContent(request.getTitle() + "에 피드백이 등록되었습니다. \n\n" + originContent);
        messageService.addMsg(dto);
    }

    /**
     * 코멘트 조회 기능
     */
    public List<FeedbackListDto> getAllComments(Long requestId){
        return messageService.findFeedbackList(requestId);
    }

    /**
     * 코멘트 삭제 기능
     */
    public void deleteComment(Long messageId){
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new EntityNotFoundException("No Such Message"));
        message.changeStat('D');
    }


    /**
     * 파일 업로드 기능
     */
    @Transactional
    public List<File> saveFile(RequestDto requestDto) throws Exception {
        List<MultipartFile> files = requestDto.getFiles();
        List<File> result = new ArrayList<>();

        try {
            if (!requestDto.getFiles().isEmpty()) {
                for (MultipartFile file1 : files) {
                    File targetFile = fileService.save(file1);
                    result.add(targetFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
