package com.erp.webtoon.service;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.domain.Message;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.domain.Webtoon;
import com.erp.webtoon.domain.WebtoonDt;
import com.erp.webtoon.dto.webtoon.FeedbackListDto;
import com.erp.webtoon.dto.webtoon.FeedbackSaveDto;
import com.erp.webtoon.dto.webtoon.WebtoonDtRequestDto;
import com.erp.webtoon.dto.webtoon.WebtoonDtResponseDto;
import com.erp.webtoon.dto.webtoon.WebtoonDtUpdateDto;
import com.erp.webtoon.repository.UserRepository;
import com.erp.webtoon.repository.WebtoonDtRepository;
import com.erp.webtoon.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final UserRepository userRepository;
    private final MessageService messageService;
    private final FileService fileService;

    /**
     * 회차 임시 업로드 (최초 등록)
     */
    public void upload(WebtoonDtRequestDto dto, MultipartFile thumbnailFile, MultipartFile episodeFile) throws IOException {
        Webtoon findWebtoon = webtoonRepository.findById(dto.getWebtoonId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 웹툰입니다."));

        User findUser = userRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 직원입니다."));

        //새로운 회차 생성
        WebtoonDt newWebtoonDt = dto.toEntity();

        newWebtoonDt.setEpisodeNum(findWebtoon.getWebtoonDts().size());
        newWebtoonDt.setManager(findUser.getName());
        newWebtoonDt.setWebtoon(findWebtoon);

        //파일 저장
        if(thumbnailFile != null) {
            File uploadThumbFile = fileService.save(thumbnailFile);
            uploadThumbFile.updateFileWebtoonDt(newWebtoonDt);
            newWebtoonDt.getFiles().add(uploadThumbFile);
        }

        if(episodeFile != null) {
            File uploadFile = fileService.save(episodeFile);
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
     * 회차 개별 조회
     */
    @Transactional(readOnly = true)
    public WebtoonDtResponseDto showOne(Long webtoonDtId) {
        WebtoonDt findWebtoonDt = webtoonDtRepository.findById(webtoonDtId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회차입니다."));

        //해당 회차 피드백들
        List<FeedbackListDto> feedbackList = findFeedbackList(webtoonDtId);

        return WebtoonDtResponseDto.builder()
                .episodeNum(findWebtoonDt.getEpisodeNum())
                .subTitle(findWebtoonDt.getSubTitle())
                .manager(findWebtoonDt.getManager())
                .content(findWebtoonDt.getContent())
                .thumbnailFileName(findWebtoonDt.getFiles().get(findWebtoonDt.getFiles().size()-2).getFileName())
                .episodeFileName(findWebtoonDt.getFiles().get(findWebtoonDt.getFiles().size()-1).getFileName())
                .feedbackList(feedbackList)
                .build();
    }

    /**
     * 회차 수정
     */
    public void update(Long webtoonDtId, WebtoonDtUpdateDto dto, MultipartFile episodeFile) throws IOException {
        WebtoonDt findWebtoonDt = webtoonDtRepository.findById(webtoonDtId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회차입니다."));

        User findUser = userRepository.findByEmployeeId(dto.getManagerId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 직원입니다."));

        //임시 업로드의 경우만 업데이트
        if(findWebtoonDt.isFinalUploadYN() == false) {

            findWebtoonDt.updateInfo(dto.getSubTitle(), dto.getContent(), findUser.getName());

            //파일 업데이트
            //만약 파일을 업데이트 하는 경우
            if (episodeFile != null) {
                // 기존의 저장된 가장 최근의 파일 상태 변경
                File file = findWebtoonDt.getFiles().get(findWebtoonDt.getFiles().size()-1);
                fileService.changeStat(file.getId());

                File uploadFile = fileService.save(episodeFile);
                uploadFile.updateFileWebtoonDt(findWebtoonDt);
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
        List<Message> feedbackList = messageService.getMessageListByRefId(webtoonDtId);

        return feedbackList.stream()
                .map(feedback -> FeedbackListDto.builder()
                        .content(feedback.getContent())
                        .sendUserName(feedback.getSendUser().getName())
                        .sendUserEmployeeId(feedback.getSendUser().getEmployeeId())
                        .build())
                .collect(Collectors.toList());
    }

    /*
       피드백 등록
       - msgType : webtoon
       - 수신자 : null
   */
    @Transactional
    public void addFeedbackMsg(FeedbackSaveDto dto) throws IOException {

        User sendUser = userRepository.findByEmployeeId(dto.getSendEmpId())
                .orElseThrow(() -> new EntityNotFoundException("메시지 발신 직원의 정보가 존재하지 않습니다."));

        //피드백 저장
        Message feedbackMsg = dto.toEntity(sendUser);
        messageService.save(feedbackMsg);

        //메시지 저장
        WebtoonDt webtoonDt = webtoonDtRepository.findById(dto.getRefId())
                .orElseThrow(() -> new EntityNotFoundException("해당하는 웹툰 회차 정보가 존재하지 않습니다."));

        String originContent = feedbackMsg.getContent();
        dto.setContent(webtoonDt.getWebtoon().getTitle() + "에 피드백이 등록되었습니다. \n\n" + originContent);
        Message message = dto.toEntity(sendUser);
        messageService.addMsg(message);
    }

}
