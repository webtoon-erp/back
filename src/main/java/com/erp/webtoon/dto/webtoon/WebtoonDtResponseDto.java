package com.erp.webtoon.dto.webtoon;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class WebtoonDtResponseDto {
    private int episodeNum;  // 회차번호

    private String subTitle; // 소제목

    private String manager; // 관리자

    private String content; // 작가의 말

    private String thumbnailFileName; // 썸네일 파일명

    private String episodeFileName; // 에피소드 파일명

    private List<FeedbackListDto> feedbackList; // 해당 회차 피드백들

}
