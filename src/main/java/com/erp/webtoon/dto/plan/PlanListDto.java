package com.erp.webtoon.dto.plan;

import com.erp.webtoon.domain.Plan;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PlanListDto {

    private String planType;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    public PlanListDto(Plan plan) {
        this.planType = plan.getPlanType();
        this.title = plan.getTitle();
        this.startDate = plan.getStartDate();
        this.endDate = plan.getEndDate();
    }
}
