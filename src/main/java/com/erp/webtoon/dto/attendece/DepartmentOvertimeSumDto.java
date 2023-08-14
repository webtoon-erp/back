package com.erp.webtoon.dto.attendece;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentOvertimeSumDto {

    private String hrOvertimeSum; // 인사부 연장근무시간 합계
    private String amOvertimeSum; // 회계부 연장근무시간 합계
    private String wtOvertimeSum; // 웹툰관리부 연장근무시간 합계
    private String itOVertimeSum; // 개발부 연장근무시간 합계

}
