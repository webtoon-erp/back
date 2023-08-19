package com.erp.webtoon.dto.plas;

import com.erp.webtoon.domain.Document;
import com.erp.webtoon.domain.DocumentData;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentDataDto {

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

    public DocumentData toEntity(Document document) {
        return DocumentData.builder()
                .fromDate(fromDate)
                .toDate(toDate)
                .deptCode(deptCode)
                .deptName(deptName)
                .company(company)
                .expenseType(expenseType)
                .count(count)
                .price(price)
                .supAmt(supAmt)
                .vatAmt(vatAmt)
                .totalAmt(totalAmt)
                .remark(remark)
                .document(document)
                .build();
    }

}
