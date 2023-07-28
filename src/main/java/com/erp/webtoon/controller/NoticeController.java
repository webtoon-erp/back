package com.erp.webtoon.controller;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.dto.notice.NoticeCardViewDto;
import com.erp.webtoon.dto.notice.NoticeRequestDto;
import com.erp.webtoon.dto.notice.NoticeResponseDto;
import com.erp.webtoon.dto.notice.NoticeUpdateDto;
import com.erp.webtoon.service.FileService;
import com.erp.webtoon.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final FileService fileService;

    /**
     * 공지사항 등록 -> 등록 후 어디로?
     */
    @PostMapping("/notice")
    public ResponseEntity save(@RequestBody NoticeRequestDto dto) throws IOException {
        noticeService.save(dto);

        return new ResponseEntity<>(redirect(), HttpStatus.MOVED_PERMANENTLY);
    }

    /**
     * 공지사항 전체 조회 (List)
     */
    @GetMapping("/notice")
    public void getAllNotice() {
        noticeService.findAllNotice();
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
    public ResponseEntity update(@PathVariable Long noticeId, @RequestBody NoticeUpdateDto dto) throws IOException {
        noticeService.update(noticeId, dto);
        return new ResponseEntity<>(redirect(), HttpStatus.MOVED_PERMANENTLY);
    }


    /**
     * 공지사항 삭제
     */
    @DeleteMapping("/notice/{noticeId}")
    public ResponseEntity delete(@PathVariable Long noticeId) {
        noticeService.delete(noticeId);
        return new ResponseEntity<>(redirect(), HttpStatus.MOVED_PERMANENTLY);
    }


    /**
     * 업로드한 파일 다운로드
     */
    @GetMapping("/download/{noticeId}/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("noticeId") Long noticeId, @PathVariable("fileId") Long fileId) throws MalformedURLException {
        File findFile = fileService.find(fileId);

        String originName = findFile.getOriginName();
        String fileName = findFile.getFileName();

        UrlResource resource = new UrlResource("file:" + fileService.getFullPath(fileName));

        String encodeFileName = UriUtils.encode(originName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodeFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    private HttpHeaders redirect() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/notice"));
        return headers;
    }
}
