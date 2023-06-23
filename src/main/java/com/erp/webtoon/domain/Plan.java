package com.erp.webtoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    private String planType;

    private String content; // 플랜 내용

    private String startDate;

    private String startTime;

    private String endDate;

    private String endTime;

    private boolean holidayYN;  // 휴일 여부
}
