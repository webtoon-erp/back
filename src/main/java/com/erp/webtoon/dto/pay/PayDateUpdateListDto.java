package com.erp.webtoon.dto.pay;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PayDateUpdateListDto {

    private String name;            // 이름

    private String employeeId;      // 사번

    private String deptName;        // 부서

    private int teamNum;            // 팀 번호

    private int monthPay;           // 급여

    private LocalDate payDate;      // 지급일

}
