package com.erp.webtoon.dto.plan;

import com.erp.webtoon.domain.Plan;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanResponseDto {

    private Long planId;

    private String title;

    private String name;

    private LocalDate registerDate;

    private LocalDate startDate;

    private LocalDate endDate;

    public PlanResponseDto(Plan plan) {
        this.planId = plan.getId();
        this.title = plan.getTitle();
        this.name = plan.getUser().getName();
        this.registerDate = plan.getRegisterDate();
        this.startDate = plan.getStartDate();
        this.endDate = plan.getEndDate();
    }
}
