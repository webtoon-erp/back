package com.erp.webtoon.service;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.domain.Notice;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.file.FileResponseDto;
import com.erp.webtoon.dto.notice.*;
import com.erp.webtoon.repository.NoticeRepository;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.*;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final FileService fileService;

    /**
     * 공지사항 등록
     */
    public void save(NoticeRequestDto dto, List<MultipartFile> files) throws IOException {
        Notice notice = dto.toEntity();

        User writeUser = userRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 직원입니다."));

        if (!dto.getDeptName().equals(writeUser.getDeptName())) {
            throw new EntityNotFoundException("부서가 일치하지 않습니다.");
        }

        notice.setWriteUser(writeUser);

        // 첨부파일이 1개 이상인 경우
        // 해당 첨부파일의 타입 지정해줘야함! -> 완료
        if (!files.isEmpty()) {
            for (MultipartFile file: files) {
                File saveFile = fileService.save(file);
                saveFile.updateFileNotice(notice);
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

        List<FileResponseDto> dtos = findNotice.getFiles().stream()
                .map(FileResponseDto::new)
                .collect(Collectors.toList());

        return NoticeResponseDto.builder()
                .title(findNotice.getTitle())
                .content(findNotice.getContent())
                .readCount(findNotice.getReadCount())
                .noticeType(findNotice.getNoticeType())
                .noticeDate(findNotice.getNoticeDate())
                .name(findNotice.getUser().getName())
                .files(dtos)
                .build();
    }


    /**
     * 공지사항 전체 조회(List)
     */
    @Transactional(readOnly = true)
    public List<NoticeListDto> findAllNotice() {
        List<Notice> noticeList = noticeRepository.findAll(Sort.by(DESC, "id"));

        return noticeList.stream()
                .map(NoticeListDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 공지사항 카드뷰 -> 6개만!
     */
    @Transactional(readOnly = true)
    public List<NoticeCardViewDto> findCardNotice() {
        Pageable pageable = PageRequest.of(0, 6, DESC, "id");

        return noticeRepository.findAll(pageable).stream()
                .map(NoticeCardViewDto::new)
                .collect(Collectors.toList());
    }


    /**
     * 공지사항 수정
     */
    public List<Long> update(Long noticeId, NoticeUpdateDto dto, List<MultipartFile> files) throws IOException {
        Notice findNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항입니다."));

        findNotice.updateNotice(dto.getNoticeType(), dto.getTitle(), dto.getContent());

        //파일 부분 추가해야함
        //공지사항에 저장된 파일목록을 지워야함 + 해당 파일의 상태가 Y인 경우 N으로 만들기
        for (File file: findNotice.getFiles()) {
            fileService.changeStat(file.getId());
        }
        findNotice.getFiles().clear();

        List<Long> fileList = new ArrayList<>();

        //해당 공지사항에 새롭게 추가
        if (!files.isEmpty()) {
            for (MultipartFile file: files) {
                File saveFile = fileService.save(file);
                findNotice.getFiles().add(saveFile);
                fileList.add(saveFile.getId());
            }
        }
        return fileList;
    }

    /**
     * 공지사항 삭제
     */
    public void delete(Long noticeId) {
        Notice findNotice = noticeRepository.findById(noticeId)
                        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항입니다."));

        noticeRepository.delete(findNotice);
    }
}
