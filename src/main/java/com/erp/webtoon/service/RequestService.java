package com.erp.webtoon.service;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.domain.Message;
import com.erp.webtoon.domain.Request;
import com.erp.webtoon.domain.RequestDt;
import com.erp.webtoon.domain.User;

import com.erp.webtoon.dto.itsm.CommentListDto;
import com.erp.webtoon.dto.itsm.CommentResponseDto;
import com.erp.webtoon.dto.itsm.ItEmployeeResponseDto;
import com.erp.webtoon.dto.itsm.RequestDeleteDto;
import com.erp.webtoon.dto.itsm.RequestDtResponseDto;
import com.erp.webtoon.dto.itsm.RequestDto;
import com.erp.webtoon.dto.itsm.RequestListResponseDto;
import com.erp.webtoon.dto.itsm.RequestRegisterResponseDto;
import com.erp.webtoon.dto.itsm.RequestResponseDto;
import com.erp.webtoon.dto.itsm.RequestStepDto;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
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
     private final FileService fileService;

    /**
     * 구매 요청 기능
     */
    @Transactional
    public RequestRegisterResponseDto purchaseRequest(RequestDto requestDto, List<MultipartFile> files) throws Exception {

        User reqUser = getReqUser(requestDto);
        User itUser = getItUser(requestDto);

        Request request = getRequest(requestDto, reqUser, itUser);
        saveFiles(files, request);

        for(int i = 0; i < requestDto.getRequestDts().size(); i++){
            RequestDt requestDt = RequestDt.builder()
                    .content(requestDto.getRequestDts().get(i).getContent())
                    .count(requestDto.getRequestDts().get(i).getCount())
                    .cost(requestDto.getRequestDts().get(i).getCost())
                    .build();
            requestDt.setRequest(request);
        }

        requestRepository.save(request);

        //연관관계
        request.updateUserRequest();

        //알림 발송
        addRequestMsg(request);

        return RequestRegisterResponseDto.builder().requestId(request.getId()).createdAt(LocalDateTime.now()).build();
    }

    /**
     * 업무 지원 요청 기능
     */
    @Transactional
    public RequestRegisterResponseDto assistRequest(RequestDto requestDto, List<MultipartFile> files) throws Exception {

        User reqUser = getReqUser(requestDto);
        User itUser = getItUser(requestDto);

        Request request = getRequest(requestDto, reqUser, itUser);
        saveFiles(files, request);
        requestRepository.save(request);

        //알림 발송
        addRequestMsg(request);

        return RequestRegisterResponseDto.builder().requestId(request.getId()).createdAt(LocalDateTime.now()).build();
    }

    /**
     * IT 담당자 리스트 조회 기능
     */
    public List<ItEmployeeResponseDto> searchItEmployee() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(user -> user.getDeptName() == "IT")
                .map(user -> ItEmployeeResponseDto.builder()
                        .employeeId(user.getEmployeeId())
                        .deptName(user.getDeptName() + user.getTeamNum())
                        .position(user.getPosition())
                        .name(user.getName())
                        .build())
                .collect(Collectors.toList());
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
        List<FileResponseDto> files = findRequest.getFiles().stream()
                .map(FileResponseDto::new)
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
                .files(files)
                .requestDtList(requestDtList)
                .build();
    }

    /**
     * 요청 삭제 기능
     */
    @Transactional
    public void deleteRequest(List<RequestDeleteDto> requestIds) {
        for (RequestDeleteDto requestDelete : requestIds) {
            Request request = requestRepository.findById(requestDelete.getRequestId())
                    .orElseThrow(() -> new EntityNotFoundException("요청이 존재하지 않습니다."));
            if(request.getStep() == 1) {
                requestRepository.deleteById(requestDelete.getRequestId());
            }
        }
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

        //알림 발송
        addRequestStepMsg(findRequest);
    }

    /**
     * 코멘트 등록 기능
     */
    @Transactional
    public CommentResponseDto registerComment(MessageSaveDto dto) {
        User sendUser = userRepository.findByEmployeeId(dto.getSendEmpId())
                .orElseThrow(() -> new EntityNotFoundException("메시지 발신 직원의 정보가 존재하지 않습니다."));

        //피드백 저장
        Message feedbackMsg = dto.toEntity(null, sendUser);
        messageService.save(feedbackMsg);

        //메시지 저장
        Request request = requestRepository.findById(feedbackMsg.getRefId())
                .orElseThrow(() -> new EntityNotFoundException("요청 정보가 존재하지 않습니다."));

        String originContent = feedbackMsg.getContent();
        dto.setContent(request.getTitle() + "에 피드백이 등록되었습니다. \n\n" + originContent);
        Message message = dto.toEntity(null, sendUser);
        messageService.addMsg(feedbackMsg);

        return CommentResponseDto.builder().messageId(feedbackMsg.getId()).createdAt(LocalDate.now()).build();
    }

    /**
     * 코멘트 조회 기능
     */
    public List<CommentListDto> getAllComments(Long requestId) {
        List<Message> commentList = messageService.getMessageListByRefId(requestId);

        return commentList.stream()
                .filter(message -> message.getRcvUser() == null)
                .map(comment -> CommentListDto.builder()
                        .content(comment.getContent())
                        .sendUserDeptName(comment.getSendUser().getDeptName())
                        .sendUserEmployeeId(comment.getSendUser().getEmployeeId())
                        .sendUserName(comment.getSendUser().getName())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 코멘트 삭제 기능
     */
    @Transactional
    public void deleteComment(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new EntityNotFoundException("No Such Message"));
        message.changeStat('D');
    }

    /**
     * 요청 등록시 알림 발송
     */
    public void addRequestMsg(Request request) {

        User rcvUser = userRepository.findByEmployeeId(request.getItUser().getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("메시지 수신 직원의 정보가 존재하지 않습니다."));
        User sendUser = userRepository.findByEmployeeId(request.getReqUser().getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("메시지 발신 직원의 정보가 존재하지 않습니다."));

        MessageSaveDto dto = MessageSaveDto.builder()
                .channel("IT")
                .content("새로운 요청이 접수되었습니다.")
                .rcvEmpId(rcvUser.getEmployeeId())
                .sendEmpId(sendUser.getEmployeeId())
                .refId(request.getId())
                .programId(null)
                .build();

        Message newMessage = dto.toEntity(rcvUser, sendUser);
        messageService.addMsg(newMessage);
    }

    /**
     * 요청 단계 변경 시 알림
     * 발신자 : 단계 변경한 사람
     * 수신자 : 요청을 요청한 사함
     */
    public void addRequestStepMsg(Request request) {

        User rcvUser = userRepository.findByEmployeeId(request.getItUser().getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("메시지 수신 직원의 정보가 존재하지 않습니다."));

        User sendUser = userRepository.findByEmployeeId(request.getReqUser().getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("메시지 발신 직원의 정보가 존재하지 않습니다."));

        MessageSaveDto dto = MessageSaveDto.builder()
                .channel("DM")
                .content("서비스 요청의 진행 단계가 변경 되었습니다.")
                .rcvEmpId(rcvUser.getEmployeeId())
                .sendEmpId(sendUser.getEmployeeId())
                .refId(request.getId())
                .programId(null)
                .build();

        Message newMessage = dto.toEntity(rcvUser, sendUser);
        messageService.addMsg(newMessage);
    }

    private User getReqUser(RequestDto requestDto) {
        return userRepository.findByEmployeeId(requestDto.getReqUserId())
                .orElseThrow(() -> new EntityNotFoundException("발신자가 존재하지 않습니다."));
    }

    private User getItUser(RequestDto requestDto) {
        return userRepository.findByEmployeeId(requestDto.getItUserId())
                .orElseThrow(() -> new EntityNotFoundException("수신자가 존재하지 않습니다."));
    }

    private Request getRequest(RequestDto requestDto, User reqUser, User itUser) {
        return Request.builder()
                .reqType(requestDto.getReqType())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .step(requestDto.getStep())
                .dueDate(requestDto.getDueDate())
                .doneDate(requestDto.getDueDate())
                .reqUser(reqUser)
                .itUser(itUser)
                .build();
    }

    private void saveFiles(List<MultipartFile> files, Request request) throws IOException {
        if (!files.isEmpty()) {
            for (MultipartFile file : files) {
                File savedFile = fileService.save(file);
                savedFile.updateFileRequest(request);
                request.getFiles().add(savedFile);
            }
        }
    }
}
