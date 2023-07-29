package com.erp.webtoon.dto.plan;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PlanUpdateDto {

    @NotBlank
    private String planType; // 이걸로 아마 부서별로 다른 캘린더가 보이도록 조절

    @NotBlank
    private String content; // 플랜 내용

    @NotNull
    private int startYear;

    @NotNull
    private int startMonth;

    @NotNull
    private int startDay;

    @NotNull
    private int startHour;
    @NotNull
    private int startMinute;

    @NotNull
    private int endYear;
    @NotNull
    private int endMonth;
    @NotNull
    private int endDay;

    @NotNull
    private int endHour;
    @NotNull
    private int endMinute;

}
