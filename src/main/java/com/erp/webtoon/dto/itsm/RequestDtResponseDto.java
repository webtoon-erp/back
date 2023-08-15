package com.erp.webtoon.dto.itsm;

import com.erp.webtoon.domain.RequestDt;
import lombok.Data;

@Data
public class RequestDtResponseDto {

    private String content; // 상세 내용

    private int count;  // 수량

    private int cost;   // 가격

    public RequestDtResponseDto(RequestDt requestDt) {
        this.content = requestDt.getContent();
        this.count = requestDt.getCount();
        this.cost = requestDt.getCost();
    }
}
