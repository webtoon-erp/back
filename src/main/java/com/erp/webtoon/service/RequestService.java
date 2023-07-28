package com.erp.webtoon.service;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.domain.Request;
import com.erp.webtoon.domain.RequestDt;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.itsm.RequestDto;
import com.erp.webtoon.repository.RequestRepository;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {

     private final UserRepository userRepository;
     private final RequestRepository requestRepository;
     private final SlackService slackService;
     private final FileService fileService;

    /**
     * 구매 요청 기능
     */
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
     * 코멘트 등록 알림 기능
     */
    public void sendCommentAlarm(Long requestId, String employeeId){
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException("No such Request"));
        String msg = request.getTitle() + "의 코멘트가 등록되었습니다.";
        slackService.sendSlackChannel(msg, employeeId);
    }


    /**
     * 파일 업로드 기능
     */
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
