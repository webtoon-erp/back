package com.erp.webtoon.dto.pay;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class PayMonthUpdateDto {

    @NotNull
    private int yearSalary;   //연봉

    @NotNull
    private int addSalary; // 추가수당


    @NotNull
    private LocalDate payDate; // 지급일

}
