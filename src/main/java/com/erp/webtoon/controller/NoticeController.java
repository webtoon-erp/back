package com.erp.webtoon.controller;

import com.erp.webtoon.dto.notice.NoticeCardViewDto;
import com.erp.webtoon.dto.notice.NoticeListDto;
import com.erp.webtoon.dto.notice.NoticeRequestDto;
import com.erp.webtoon.dto.notice.NoticeResponseDto;
import com.erp.webtoon.dto.notice.NoticeUpdateDto;
import com.erp.webtoon.service.NoticeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 공지사항 등록
     */
    @PostMapping("/notice")
    public ResponseEntity<Result> save(@RequestPart NoticeRequestDto dto, @RequestPart("files")List<MultipartFile> files) throws IOException {
        noticeService.save(dto, files);

        return ResponseEntity.ok(new Result(redirect(), null));
    }

    /**
     * 공지사항 전체 조회 (List)
     */
    @GetMapping("/notice")
    public ResponseEntity getAllNotice() {

        List<NoticeListDto> allNotice = noticeService.findAllNotice();
        return ResponseEntity.ok(allNotice);
    }

    /**
     * 공지사항 개별 조회
     */
    @GetMapping("/notice/{noticeId}")
    public ResponseEntity getNotice(@PathVariable("noticeId") Long noticeId) {
        NoticeResponseDto responseDto = noticeService.find(noticeId);

        return ResponseEntity.ok(responseDto);
    }


    /**
     * 공지사항 카드뷰 조회 (메인화면에서)
     */
    @GetMapping("/home")
    public ResponseEntity getCardNotice() {
        List<NoticeCardViewDto> dtoList = noticeService.findCardNotice();

        return new ResponseEntity(dtoList, HttpStatus.OK);
    }

    /**
     * 공지사항 수정 (리다이렉트)
     */
    @PutMapping("/notice/{noticeId}")
    public ResponseEntity<Result> update(@PathVariable Long noticeId, @RequestPart NoticeUpdateDto dto, @RequestPart List<MultipartFile> files ) throws IOException {
        List<Long> fileIds = noticeService.update(noticeId, dto, files);

        return ResponseEntity.ok(new Result(redirect(), fileIds));
    }


    /**
     * 공지사항 삭제
     */
    @DeleteMapping("/notice/{noticeId}")
    public ResponseEntity delete(@PathVariable Long noticeId) {
        noticeService.delete(noticeId);

        return ResponseEntity.ok(new Result(redirect(), null));
    }

    private URI redirect() {
        return URI.create("/notice");
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T url;
        private T info;
    }
}
