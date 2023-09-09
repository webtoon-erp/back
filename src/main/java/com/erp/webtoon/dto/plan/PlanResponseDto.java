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
    private String planType;

    private String title;

    private LocalDate startDate;

    private LocalTime startTime;

    private LocalDate endDate;

    private LocalTime endTime;

    private boolean holidayYN;

    public PlanResponseDto(Plan plan) {
        this.planType = plan.getPlanType();
        this.title = plan.getTitle();
        this.startDate = plan.getStartDate();
        this.startTime = plan.getStartTime();
        this.endDate = plan.getEndDate();
        this.endTime = plan.getEndTime();
        this.holidayYN = plan.isHolidayYN();
    }
}
