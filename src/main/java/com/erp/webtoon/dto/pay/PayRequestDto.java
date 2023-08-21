package com.erp.webtoon.dto.pay;

import com.erp.webtoon.domain.Pay;
import com.erp.webtoon.domain.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class PayRequestDto {

    //연봉, 추가 수당, 지급계좌
    //월급은 연봉으로 계산
    //자격수당은 자동 불러오기 계산
    @NotBlank
    private String employeeId; // 사번

    @NotNull
    private int yearSalary;     //연봉

    @NotNull
    private int addSalary; // 추가수당

    @NotBlank
    private String bankAccount; // 지급계좌

    private LocalDate payDate; // 지급일

    public Pay toEntity() {
        return Pay.builder()
                .salary(yearSalary)
                .addPay(addSalary)
                .bankAccount(bankAccount)
                .payDate(payDate)
                .build();
    }
}
