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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
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

        return new ResponseEntity(id, headers, HttpStatus.MOVED_PERMANENTLY);
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
    public ResponseEntity<Result> getWebtoon(@PathVariable("webtoonId") Long webtoonId) throws MalformedURLException {
        WebtoonResponseDto webtoon = webtoonService.getOneWebtoon(webtoonId);

        // 썸네일 이미지 조회
        UrlResource resource = new UrlResource("file:" + fileService.getFullPath(webtoon.getThumbnailFileName()));

        return ResponseEntity.ok(new Result(resource, webtoon));

    }

    /**
     * 제목 웹툰 검색
     */
    @GetMapping("/webtoon/{title}")
    public ResponseEntity showByTitle(@PathVariable String title) {

        List<WebtoonListResponseDto> dtos = webtoonService.getTitleWebtoon(title);

        return ResponseEntity.ok(dtos);

    }

    /**
     * 작가 웹툰 검색
     */
    @GetMapping("/webtoon/{artist}")
    public ResponseEntity showByArtist(@PathVariable String artist) {

        List<WebtoonListResponseDto> dtos = webtoonService.getTitleWebtoon(artist);

        return ResponseEntity.ok(dtos);
    }

    /**
     * 카테고리 웹툰 검색
     */
    @GetMapping("/webtoon/{category}")
    public ResponseEntity showByCategory(@PathVariable String category) {

        List<WebtoonListResponseDto> dtos = webtoonService.getTitleWebtoon(category);

        return ResponseEntity.ok(dtos);

    }

    /**
     * 키워드 웹툰 검색
     */
    @GetMapping("/webtoon/{keyword}")
    public ResponseEntity showByKeyword(@PathVariable String keyword) {

        List<WebtoonListResponseDto> dtos = webtoonService.getTitleWebtoon(keyword);

        return ResponseEntity.ok(dtos);
    }

    /**
     * 웹툰 수정
     */
    @PutMapping("/webtoon/{webtoonId}")
    public ResponseEntity update(@PathVariable Long webtoonId, @RequestBody WebtoonUpdaateDto dto) throws IOException {
        webtoonService.update(webtoonId, dto);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/webtoon/{webtoonId}"));

        return new ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T Resource;
        private T info;
    }
}
