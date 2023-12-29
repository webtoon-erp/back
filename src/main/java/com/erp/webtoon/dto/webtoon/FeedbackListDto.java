package com.erp.webtoon.dto.webtoon;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class FeedbackListDto {

    private LocalDateTime createdDate; // 발신 일시

    private String content;     // 메세지 내용

    private String sendUserName; // 발신자 이름

    private String sendUserEmployeeId; // 발신자 사번

}
