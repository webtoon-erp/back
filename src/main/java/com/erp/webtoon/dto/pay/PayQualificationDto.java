package com.erp.webtoon.dto.pay;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayQualificationDto {

    private String name; // 자격증 명

    private int money; // 자격 수당
}
