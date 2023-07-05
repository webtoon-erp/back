package com.erp.webtoon.dto.webtoon;

import com.erp.webtoon.domain.File;
import lombok.Builder;
import lombok.Data;

import java.util.List;

//웹툽 개별 상세 조회 Dto
@Data
@Builder
public class WebtoonDtResponseDto {
    private String title;

    private String intro;

    private String artist;

    private String category;

    private String keyword;

    private File thumnailFile; // 썸네일 파일

    private List<String> episode; // 각 웹툰별 회차

}
