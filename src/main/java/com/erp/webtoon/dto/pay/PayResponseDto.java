package com.erp.webtoon.dto.pay;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PayResponseDto {

    private String employeeId; // 사번

    private String name;    // 이름

    private String email;   // 이메일

    private String deptName;    //부서명

    private String position;    // 직급

    private String bankAccount; // 지급계좌

    private int yearSalary;   // 연봉

    private int monthSalary; // 월급

    private List<PayListResponseDto> payList; //지급 목록
}
