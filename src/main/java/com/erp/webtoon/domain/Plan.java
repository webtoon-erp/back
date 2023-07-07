package com.erp.webtoon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

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

    private LocalDate startDate;

    private LocalTime startTime;

    private LocalDate endDate;

    private LocalTime endTime;

    private boolean holidayYN;  // 휴일 여부

    @Builder
    public Plan(String planType, String content, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, boolean holidayYN) {
        this.planType = planType;
        this.content = content;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.holidayYN = holidayYN;
    }
}
