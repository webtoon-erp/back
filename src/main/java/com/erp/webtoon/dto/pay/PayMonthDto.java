package com.erp.webtoon.dto.pay;

import com.erp.webtoon.domain.Pay;
import lombok.Builder;
import lombok.Data;

@Data
public class PayMonthDto {
    private String bankAccount; // 지급계좌

    private int yearSalary;   // 연봉

    private int monthSalary; // 월급

    private int addSalary; // 추가 수당

    private int qualSalary; // 자격 수당

    public PayMonthDto(Pay pay) {
        this.bankAccount = pay.getBankAccount();
        this.yearSalary = pay.getSalary();
        this.monthSalary = (pay.getSalary() / 12);
        this.addSalary = pay.getAddPay();
        this.qualSalary = 0;
    }
}
