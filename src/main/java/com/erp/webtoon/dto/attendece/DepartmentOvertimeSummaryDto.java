package com.erp.webtoon.dto.attendece;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentOvertimeSummaryDto  {

    private String hrOvertime; // 인사부 연장근무시간
    private String amOvertime; // 회계부 연장근무시간
    private String wtOvertime; // 웹툰관리부 연장근무시간
    private String itOVertime; // 개발부 연장근무시간

}
