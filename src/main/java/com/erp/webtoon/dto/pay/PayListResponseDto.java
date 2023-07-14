package com.erp.webtoon.dto.pay;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PayListResponseDto {

    private int totalMonthSalary; // 월 지급 총합

    private int addSalary; // 추가수당

    private LocalDate payDate; // 지급날짜

    private boolean payYN; // 지급여부
}
