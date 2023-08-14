package com.erp.webtoon.dto.itsm;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestDtDto {
    private int sortSequence; // 정렬번호

    private String content; // 상세 내용

    private int count;

    private int cost;

}
