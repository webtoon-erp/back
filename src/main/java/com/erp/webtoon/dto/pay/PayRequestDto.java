package com.erp.webtoon.dto.pay;

import com.erp.webtoon.domain.Pay;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PayRequestDto {

    //연봉, 추가 수당, 지급계좌, 지급일
    //월급은 연봉으로 계산

    private int yearSalary;

    private int addSalary; // 추가수당

    private String bankAccount; // 지급계좌

    private int payDateYear; // 지급 년도

    private int payDateMonth; // 지급 월

    private int payDateDay; // 지급 일

    public Pay toEntity() {
        return Pay.builder()
                .salary(yearSalary)
                .addPay(addSalary)
                .bankAccount(bankAccount)
                .payDate(LocalDate.of(payDateYear, payDateMonth, payDateDay))
                .payYN(false).build();
    }
}
