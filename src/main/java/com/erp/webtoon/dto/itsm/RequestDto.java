package com.erp.webtoon.dto.itsm;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class RequestDto {
    private String reqType;

    private String title;

    private String content;

    private int step;

    private LocalDate dueDate;     // 기한 일자

    private LocalDate doneDate;    // 완료 일자

    private String reqUserId;   // 요청자 사번

    private String itUserId;    // 담당자 사번

    private List<RequestDtDto> requestDts;
}
