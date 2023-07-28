package com.erp.webtoon.dto.webtoon;

import lombok.Builder;
import lombok.Data;

import java.util.List;

//웹툽 개별 상세 조회 Dto
@Data
@Builder
public class WebtoonResponseDto {
    private String title;

    private String intro;

    private String artist;

    private String illustrator;

    private String category;

    private String keyword;

    private String thumbnailFileName; // 썸네일 파일명

    private List<WebtoonEpisodeDto> episode; // 각 웹툰별 회차

}
