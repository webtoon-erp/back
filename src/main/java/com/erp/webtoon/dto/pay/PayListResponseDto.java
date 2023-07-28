package com.erp.webtoon.dto.pay;

import com.erp.webtoon.domain.Pay;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PayListResponseDto {

    private LocalDate payDate; // 지급날짜

    private int totalMonthSalary; // 월 지급 총합

    private boolean payYN; // 지급여부

    public PayListResponseDto(Pay pay) {
        this.totalMonthSalary = pay.getSalary() + pay.getAddPay();
        this.payDate = pay.getPayDate();
        this.payYN = pay.isPayYN();
    }
}
