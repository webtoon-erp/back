package com.erp.webtoon.dto.webtoon;

import com.erp.webtoon.dto.message.FeedbackListDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WebtoonDtResponseDto {
    private int episodeNum;  // 회차번호

    private String subTitle; // 소제목

    private String content; // 작가의 말

    private String fileName; // 업로드 파일명

    private List<FeedbackListDto> feedbackList; // 해당 회차 피드백들

}
