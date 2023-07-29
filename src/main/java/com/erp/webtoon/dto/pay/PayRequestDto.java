package com.erp.webtoon.dto.pay;

import com.erp.webtoon.domain.Pay;
import com.erp.webtoon.domain.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PayRequestDto {

    //연봉, 추가 수당, 지급계좌, 지급일
    //월급은 연봉으로 계산
    private String employeeId; // 사번

    private int yearSalary;     //연봉

    private int addSalary; // 추가수당

    private String bankAccount; // 지급계좌

    private LocalDate payDate; // 지급일

    public Pay toEntity() {
        return Pay.builder()
                .salary(yearSalary)
                .addPay(addSalary)
                .bankAccount(bankAccount)
                .payDate(payDate)
                .payYN(false).build();
    }
}
