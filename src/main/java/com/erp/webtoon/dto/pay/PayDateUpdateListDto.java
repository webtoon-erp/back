package com.erp.webtoon.dto.pay;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PayDateUpdateListDto {

    private String employeeId;

    private LocalDate payDate;

}
