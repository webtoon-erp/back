package com.erp.webtoon.dto.plan;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class PlanResponseDto {
    private String planType;

    private String content;

    private LocalDate startDate;

    private LocalTime startTime;

    private LocalDate endDate;

    private LocalTime endTime;

    private boolean holidayYN;
}
