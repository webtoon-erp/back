package com.erp.webtoon.dto.pay;

import lombok.Data;

@Data
public class QualificationPayRequestDto {

    private Long qualId;

    private String name;    //자격증 명

    private int qualPay; // 자격수당
}
