package com.erp.webtoon.controller;

import com.erp.webtoon.dto.webtoon.WebtoonDtRequestDto;
import com.erp.webtoon.dto.webtoon.WebtoonDtUpdateDto;
import com.erp.webtoon.service.FileService;
import com.erp.webtoon.service.WebtoonDtService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
public class WebtoonDtController {

    private final WebtoonDtService webtoonDtService;

    private final FileService fileService;

    /**
     * 개별 웹툰 에피소드 등록
     */
    @PostMapping("/webtoonDt")
    public void upload(@RequestBody WebtoonDtRequestDto dto) throws IOException {
        webtoonDtService.upload(dto);
    }

    /**
     * 최종 업로드
     */
    @PostMapping("/webtoonDt/{webtoonDtId}")
    public void uploadFinal(@PathVariable Long webtoonDtId) {
        webtoonDtService.finalUpload(webtoonDtId);
    }

    /**
     * 회차 별 조회 -> 이미지만 조회 가능하도록,,,(추후 프론트랑 논의)
     */
    @GetMapping("/image/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable("fileName") String fileName) throws MalformedURLException {
        UrlResource resource = new UrlResource("file:" + fileService.getFullPath(fileName));

        return ResponseEntity.ok(resource);
    }

    /**
     * 회차 수정
     */
    @PutMapping("/webtoonDt/{webtoonDtId}")
    public void update(@PathVariable Long webtoonDtId, @RequestBody WebtoonDtUpdateDto dto) throws IOException {
        webtoonDtService.update(webtoonDtId, dto);
    }

    /**
     * 회차 삭제
     */
    @DeleteMapping("webtoonDt/{webtoonDtId{")
    public void delete(@PathVariable Long webtoonDtId) {
        webtoonDtService.delete(webtoonDtId);
    }
}
