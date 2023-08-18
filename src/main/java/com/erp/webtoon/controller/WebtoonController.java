package com.erp.webtoon.controller;

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
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity save(@RequestBody WebtoonRequestDto dto) throws IOException {
        Long id = webtoonService.save(dto);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/webtoon"));

        HashMap<String, Long> body = new HashMap<>();
        body.put("id", id);

        return new ResponseEntity(body, headers, HttpStatus.MOVED_PERMANENTLY);
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
     * 제목 웹툰 검색
     */
    @GetMapping("/webtoon/one/{title}")
    public ResponseEntity showByTitle(@PathVariable("title") String title) {

        List<WebtoonListResponseDto> dtos = webtoonService.getTitleWebtoon(title);

        return ResponseEntity.ok(dtos);

    }

    /**
     * 작가 웹툰 검색
     */
    @GetMapping("/webtoon/one/{artist}")
    public ResponseEntity showByArtist(@PathVariable("artist") String artist) {

        List<WebtoonListResponseDto> dtos = webtoonService.getTitleWebtoon(artist);

        return ResponseEntity.ok(dtos);
    }

    /**
     * 카테고리 웹툰 검색
     */
    @GetMapping("/webtoon/one/{category}")
    public ResponseEntity showByCategory(@PathVariable("category") String category) {

        List<WebtoonListResponseDto> dtos = webtoonService.getTitleWebtoon(category);

        return ResponseEntity.ok(dtos);

    }

    /**
     * 키워드 웹툰 검색
     */
    @GetMapping("/webtoon/one/{keyword}")
    public ResponseEntity showByKeyword(@PathVariable("keyword") String keyword) {

        List<WebtoonListResponseDto> dtos = webtoonService.getTitleWebtoon(keyword);

        return ResponseEntity.ok(dtos);
    }

    /**
     * 웹툰 수정
     */
    @PutMapping("/webtoon/{webtoonId}")
    public ResponseEntity update(@PathVariable Long webtoonId, @RequestPart("dto") WebtoonUpdaateDto dto, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        webtoonService.update(webtoonId,file, dto);

        HttpHeaders headers = new HttpHeaders();
        URI location = UriComponentsBuilder.newInstance()
                .path("/webtoon/{webtoonId}")
                .buildAndExpand(webtoonId).toUri();

        headers.setLocation(location);

        return new ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T Resource;
        private T info;
    }
}
