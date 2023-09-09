package com.erp.webtoon.dto.plan;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PlanUpdateDto {

    @NotBlank
    private String planType;

    @NotBlank
    private String title;

    private LocalDate startDate;

    private LocalTime startTime;

    private LocalDate endDate;

    private LocalTime endTime;
}
