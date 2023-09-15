package com.erp.webtoon.dto.itsm;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestDtDto {

    private String content; // 상세 내용

    private int count;

    private int cost;
}
