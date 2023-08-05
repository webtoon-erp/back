package com.erp.webtoon.dto.webtoon;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class WebtoonDtListDto {
    private int episodeNum;  // 회차번호

    private String subTitle; // 소제목

    private LocalDate uploadDate; // 업로드 날짜

    private String manager; // 담당자

    private boolean finalUploadYN; // 임시 업로드(false) 혹은 최종 업로드(true)
}
