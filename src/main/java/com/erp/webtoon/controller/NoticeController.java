package com.erp.webtoon.controller;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.dto.file.FileSaveDto;
import com.erp.webtoon.repository.FileRepository;
import com.erp.webtoon.service.FileService;
import com.erp.webtoon.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import javax.persistence.EntityNotFoundException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final FileService fileService;

    /**
     * 공지사항 등록
     */

    /**
     * 공지사항 수정
     */

    /**
     * 공지사항 전체 조회
     */

    /**
     * 공지사항 조회
     */

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
}
