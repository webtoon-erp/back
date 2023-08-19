package com.erp.webtoon.dto.attendance;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentOvertimeAvgDto {

    private String hrOvertimeAvg; // 인사부 연장근무시간 평균
    private String amOvertimeAvg; // 회계부 연장근무시간 평균
    private String wtOvertimeAvg; // 웹툰관리부 연장근무시간 평균
    private String itOVertimeAvg; // 개발부 연장근무시간 평균

}
