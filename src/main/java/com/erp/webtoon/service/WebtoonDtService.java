package com.erp.webtoon.service;

import com.erp.webtoon.domain.*;
import com.erp.webtoon.dto.message.MessageSaveDto;
import com.erp.webtoon.dto.webtoon.FeedbackListDto;
import com.erp.webtoon.dto.webtoon.FeedbackSaveDto;
import com.erp.webtoon.dto.webtoon.WebtoonDtRequestDto;
import com.erp.webtoon.dto.webtoon.WebtoonDtUpdateDto;
import com.erp.webtoon.repository.MessageRepository;
import com.erp.webtoon.repository.UserRepository;
import com.erp.webtoon.repository.WebtoonDtRepository;
import com.erp.webtoon.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WebtoonDtService {

    private final WebtoonDtRepository webtoonDtRepository;

    private final WebtoonRepository webtoonRepository;
    private final FileService fileService;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;

    /**
     * 회차 임시 업로드 (최초 등록)
     */
    public void upload(WebtoonDtRequestDto dto) throws IOException {
        Webtoon findWebtoon = webtoonRepository.findById(dto.getWebtoonId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 웹툰입니다."));

        WebtoonDt newWebtoonDt = dto.toEntity(findWebtoon);

        //파일 저장
        if(!dto.getUploadFile().isEmpty()) {
            File uploadFile = fileService.save(dto.getUploadFile());
            uploadFile.updateFileWebtoonDt(newWebtoonDt);
            newWebtoonDt.getFiles().add(uploadFile);
        }

        webtoonDtRepository.save(newWebtoonDt);

    }

    /**
     * 회차 최종 업로드
     */
    public void finalUpload(Long webtoonDtId) {
        WebtoonDt findWebtoonDt = webtoonDtRepository.findById(webtoonDtId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회차입니다."));

        findWebtoonDt.changeUploadState();

    }


    /**
     * 회차 수정
     */
    public void update(Long webtoonDtId, WebtoonDtUpdateDto dto) throws IOException {
        WebtoonDt findWebtoonDt = webtoonDtRepository.findById(webtoonDtId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회차입니다."));

        //임시 업로드의 경우만 업데이트
        if(findWebtoonDt.isFinalUploadYN() == false) {
            findWebtoonDt.updateInfo(dto.getEpisodeNum(), dto.getSubTitle());

            //파일 업데이트
            for (File file : findWebtoonDt.getFiles()) {
                fileService.changeStat(file);
            }
            findWebtoonDt.getFiles().clear();

            if (!dto.getUploadFile().isEmpty()) {
                File uploadFile = fileService.save(dto.getUploadFile());
                findWebtoonDt.getFiles().add(uploadFile);
            }
        }
    }


    /**
     * 회차 삭제
     */
    public void delete(Long webtoonDtId) {
        WebtoonDt findWebtoonDt = webtoonDtRepository.findById(webtoonDtId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회차입니다."));

        webtoonDtRepository.delete(findWebtoonDt);
    }

    /*
        피드백 조회
        - msgType : webtoon
        - 수신자 : null
    */
    @Transactional(readOnly = true)
    public List<FeedbackListDto> findFeedbackList(Long webtoonDtId) {
        List<Message> feedbackList = messageRepository.findByRefId(webtoonDtId);

        return feedbackList.stream()
                .map(feedback -> FeedbackListDto.builder()
                        .content(feedback.getContent())
                        .sendUser(feedback.getSendUser())
                        .build())
                .collect(Collectors.toList());
    }

    /*
        피드백 등록
        - msgType : webtoon
        - 수신자 : null
    */
    public void addFeedback(FeedbackSaveDto dto) throws IOException {

        User sendUser = userRepository.findByEmployeeId(dto.getSendEmpId())
                .orElseThrow(() -> new EntityNotFoundException("메시지 발신 직원의 정보가 존재하지 않습니다."));

        //피드백 저장
        Message feedbackMsg = dto.toEntity(sendUser);
        messageRepository.save(feedbackMsg);

        //메시지 저장
        Webtoon webtoon = webtoonRepository.findById(feedbackMsg.getRefId())
                .orElseThrow(() -> new EntityNotFoundException("웹툰 정보가 존재하지 않습니다."));

        String originContent = feedbackMsg.getContent();
        dto.setContent(webtoon.getTitle() + "에 피드백이 등록되었습니다. \n\n" + originContent);

        MessageSaveDto msgDto = new MessageSaveDto();
        msgDto.setMsgType(dto.getMsgType());
        msgDto.setContent(dto.getContent());
        msgDto.setRefId(dto.getRefId());
        msgDto.setProgramId(dto.getProgramId());
        msgDto.setSendEmpId(dto.getSendEmpId());
        msgDto.setRcvEmpId(null);

        messageService.addMsg(msgDto);

    }

}
