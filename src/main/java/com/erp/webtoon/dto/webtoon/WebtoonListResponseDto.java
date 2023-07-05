package com.erp.webtoon.dto.webtoon;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebtoonListResponseDto {

    private Long id;
    private String title;

    private String artist;  //작가 이름

    private String category;

    private String keyword;

    // 썸네일 파일은 상세조회 시에만 보이도록!...
}
