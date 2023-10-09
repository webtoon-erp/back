package com.erp.webtoon.dto.pay;

import com.erp.webtoon.domain.Pay;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PayListResponseDto {

    private String payMonth;   // 지급월

    private LocalDate payDate; // 지급날짜

    private int totalMonthSalary; // 월 지급 총합

    public PayListResponseDto(Pay pay) {
        this.payMonth = getPayDate().toString().substring(0, 4);
        this.totalMonthSalary = pay.getSalary() + pay.getAddPay();
        this.payDate = pay.getPayDate();
    }
}
