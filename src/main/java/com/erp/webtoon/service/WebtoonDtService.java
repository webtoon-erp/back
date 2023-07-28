package com.erp.webtoon.service;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.domain.Webtoon;
import com.erp.webtoon.domain.WebtoonDt;
import com.erp.webtoon.dto.webtoon.WebtoonDtRequestDto;
import com.erp.webtoon.dto.webtoon.WebtoonDtUpdateDto;
import com.erp.webtoon.repository.UserRepository;
import com.erp.webtoon.repository.WebtoonDtRepository;
import com.erp.webtoon.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class WebtoonDtService {

    private final WebtoonDtRepository webtoonDtRepository;
    private final WebtoonRepository webtoonRepository;
    private final UserRepository userRepository;
    private final FileService fileService;


    /**
     * 회차 임시 업로드 (최초 등록)
     */
    public void upload(WebtoonDtRequestDto dto) throws IOException {
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

        User findUser = userRepository.findByEmployeeId(dto.getManagerId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 직원입니다."));

        //임시 업로드의 경우만 업데이트
        if(findWebtoonDt.isFinalUploadYN() == false) {

            findWebtoonDt.updateInfo(dto.getSubTitle(), dto.getContent(), findUser.getName());

            //파일 업데이트
            //만약 파일을 업데이트 하는 경우
            if (!dto.getUploadFile().isEmpty()) {
                // 기존의 저장된 가장 최근의 파일 상태 변경
                File file = findWebtoonDt.getFiles().get(-1);
                fileService.changeStat(file);

                File uploadFile = fileService.save(dto.getUploadFile());
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

}
