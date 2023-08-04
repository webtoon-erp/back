package com.erp.webtoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class DocumentData {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_data_id")
    private Long id;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id")
    private Document document;



}
