package com.erp.webtoon.controller;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 업로드한 파일 다운로드
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Long fileId) throws MalformedURLException {
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
