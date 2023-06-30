package com.erp.webtoon.service;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.domain.Notice;
import com.erp.webtoon.dto.notice.NoticeListDto;
import com.erp.webtoon.dto.notice.NoticeRequestDto;
import com.erp.webtoon.dto.notice.NoticeResponseDto;
import com.erp.webtoon.dto.notice.NoticeUpdateDto;
import com.erp.webtoon.repository.NoticeRepository;
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
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final FileService fileService;

    /**
     * 공지사항 등록
     */
    public void save(NoticeRequestDto dto) throws IOException {
        Notice notice = dto.toEntity();

        // 첨부파일이 1개 이상인 경우
        if (!dto.getUploadFiles().isEmpty()) {
            for (MultipartFile file: dto.getUploadFiles()) {
                File saveFile = fileService.save(file);
                notice.getFiles().add(saveFile);
            }
        }

        noticeRepository.save(notice);
    }

    /**
     * 공지사항 개별 조회
     */
    public NoticeResponseDto find(Long noticeId) {
        Notice findNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항 입니다."));

        // 공지사항 조회수 증가
        findNotice.addReadCount();

        return NoticeResponseDto.builder()
                .noticeType(findNotice.getNoticeType())
                .title(findNotice.getTitle())
                .content(findNotice.getContent())
                .readCount(findNotice.getReadCount())
                .originFileNames(findNotice.getFileNames())
                .build();
    }



    /**
     * 공지사항 전체 조회(List)
     */
    public List<NoticeListDto> findAll() {
        List<Notice> noticeList = noticeRepository.findAll();

        return noticeList.stream()
                .map(notice -> NoticeListDto.builder()
                        .noticeType(notice.getNoticeType())
                        .title(notice.getTitle())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 공지사항 수정
     */

    public void update(Long noticeId, NoticeUpdateDto dto) throws IOException {
        Notice findNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항입니다."));

        dto.getNoticeType().ifPresent(findNotice::updateNoticeType);
        dto.getTitle().ifPresent(findNotice::updateTitle);
        dto.getContent().ifPresent(findNotice::updateContent);

        //파일 부분 추가해야함
        //공지사항에 저장된 파일목록을 지워야함 + 해당 파일의 상태가 Y인 경우 N으로 만들기
        for (File file: findNotice.getFiles()) {
            fileService.changeStat(file);
        }
        findNotice.getFiles().clear();

        //해당 공지사항에 새롭게 추가
        if (!dto.getUploadFiles().isEmpty()) {
            for (MultipartFile file: dto.getUploadFiles()) {
                File saveFile = fileService.save(file);
                findNotice.getFiles().add(saveFile);
            }
        }
    }

    /**
     * 공지사항 삭제
     */
    public void delete(Notice notice) {
        noticeRepository.delete(notice);
    }
}
