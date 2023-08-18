package com.erp.webtoon.dto.plas;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentDataRequestDto {

    private LocalDateTime fromDate; // 시작일시

    private LocalDateTime toDate;   // 종료일시

    private String deptCode;        // 관련 부서코드

    private String deptName;        // 관련 부서명

    private String company;         // 관련 거래처

    private String expenseType;     // 비용 타입

    private int count;              // 수량

    private int price;              // 단가

    private int supAmt;             // 공급가액

    private int vatAmt;             // 부가세액

    private int totalAmt;           // 총금액

    private String remark;          // 비고

}
