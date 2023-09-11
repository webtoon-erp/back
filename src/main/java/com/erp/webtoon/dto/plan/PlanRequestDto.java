package com.erp.webtoon.dto.plan;

import com.erp.webtoon.domain.Plan;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PlanRequestDto {

    private String employeeId; // 일정 작성한 직원

    private String planType; // 이걸로 아마 부서별로 다른 캘린더가 보이도록 조절

    private String title; // 플랜 내용

    private LocalDate startDate;

    private LocalTime startTime;

    private LocalDate endDate;

    private LocalTime endTime;

    private boolean holidayYN;  // 휴일 여부

    public Plan toEntity() {
        return Plan.builder()
                .planType(planType)
                .title(title)
                .month(startDate.getMonthValue())
                .startDate(startDate)
                .startTime(startTime)
                .endDate(endDate)
                .endTime(endTime)
                .holidayYN(holidayYN)
                .build();
    }
}
