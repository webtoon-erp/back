package com.erp.webtoon.dto.pay;

import com.erp.webtoon.domain.Qualification;
import lombok.Builder;
import lombok.Data;

@Data
public class PayQualificationDto {

    private String name; // 자격증 명

    private int money; // 자격 수당

    @Builder
    public PayQualificationDto(Qualification qualification) {
        this.name = qualification.getQlfcType();
        this.money = qualification.getQlfcPay();
    }
}
