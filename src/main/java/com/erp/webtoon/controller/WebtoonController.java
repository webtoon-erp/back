package com.erp.webtoon.controller;

import com.erp.webtoon.dto.webtoon.WebtoonAllCardViewDto;
import com.erp.webtoon.dto.webtoon.WebtoonListResponseDto;
import com.erp.webtoon.dto.webtoon.WebtoonRequestDto;
import com.erp.webtoon.dto.webtoon.WebtoonResponseDto;
import com.erp.webtoon.dto.webtoon.WebtoonUpdaateDto;
import com.erp.webtoon.service.FileService;
import com.erp.webtoon.service.WebtoonService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class WebtoonController {

    private final WebtoonService webtoonService;
    private final FileService fileService;

    /**
     * 웹툰 등록
     */
    @PostMapping("/webtoon")
    public ResponseEntity save(@RequestPart("dto") WebtoonRequestDto dto, @RequestPart("file") MultipartFile file) throws IOException {
        Long id = webtoonService.save(dto, file);

        URI uri = URI.create("/webtoon");

        HashMap<String, Long> body = new HashMap<>();
        body.put("id", id);

        return ResponseEntity.ok(new Result(uri, body));
    }

    /**
     * 웹툰 카드뷰
     */
    @GetMapping("/webtoon/cardview")
    public ResponseEntity showCardView() {
        WebtoonAllCardViewDto weekWebtoon = webtoonService.getWeekWebtoon();
        return ResponseEntity.ok(weekWebtoon);
    }


    /**
     * 등록된 모든 웹툰 조회
     */
    @GetMapping("/webtoon")
    public ResponseEntity showAll() {
        List<WebtoonListResponseDto> dtos = webtoonService.getAllWebtoon();

        return ResponseEntity.ok(dtos);
    }


    /**
     * 특정 웹툰 조회
     */
    @GetMapping("/webtoon/{webtoonId}")
    public ResponseEntity getWebtoon(@PathVariable("webtoonId") Long webtoonId) throws MalformedURLException {
        WebtoonResponseDto webtoon = webtoonService.getOneWebtoon(webtoonId);

        // 썸네일 이미지 조회
        UrlResource resource = new UrlResource("file:" + fileService.getFullPath(webtoon.getThumbnailFileName()));
        return ResponseEntity.ok(new Result(resource.getURL(), webtoon));

    }

    /**
     * 웹툰 수정
     */
    @PutMapping("/webtoon/{webtoonId}")
    public ResponseEntity update(@PathVariable Long webtoonId, @RequestPart("dto") WebtoonUpdaateDto dto, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        webtoonService.update(webtoonId,file, dto);

        URI url = UriComponentsBuilder.newInstance()
                .path("/webtoon/{webtoonId}")
                .buildAndExpand(webtoonId).toUri();

        return ResponseEntity.ok(new Result(url, null));
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T url;
        private T info;
    }
}
