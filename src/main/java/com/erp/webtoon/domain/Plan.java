package com.erp.webtoon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    private String title; // 플랜 내용

    private int month; // 해당 계획 월 정보

    private LocalDate startDate;

    private LocalTime startTime;

    private LocalDate endDate;

    private LocalTime endTime;

    private LocalDate registerDate; // 등록 시간

    private boolean holidayYN;  // 휴일 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Plan(String planType, String title, int month, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, boolean holidayYN) {
        this.planType = planType;
        this.title = title;
        this.month = month;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.registerDate = LocalDate.now();
        this.holidayYN = holidayYN;
    }

    public void updatePlan(String planType, String title, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        this.planType = planType;
        this.title = title;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
    }

    // -- 연관관계 메소드 -- //
    public void regisUser(User user) {
        this.user = user;
        user.getPlans().add(this);
    }
}
