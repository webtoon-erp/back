package com.erp.webtoon.dto.plan;

import com.erp.webtoon.domain.Plan;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PlanRequestDto {

    private String planType;

    private String content; // 플랜 내용

    private int startYear;
    private int startMonth;
    private int startDay;

    private int startHour;
    private int startMinute;

    private int endYear;
    private int endMonth;
    private int endDay;

    private int endHour;
    private int endMinute;

    private boolean holidayYN;  // 휴일 여부

    public Plan toEntity() {
        return Plan.builder()
                .planType(planType)
                .content(content)
                .startDate(LocalDate.of(startYear, startMonth, startDay))
                .startTime(LocalTime.of(startHour, startMinute, 0))
                .endDate(LocalDate.of(endYear, endMonth, endYear))
                .endTime(LocalTime.of(endHour, endMinute, 0))
                .holidayYN(holidayYN)
                .build();
    }
}
