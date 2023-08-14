package com.erp.webtoon.dto.attendece;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TotalAttendenceResponseDto {

    private TotalAttendenceSummaryDto totalAttendenceSummaryDto;

    private MonthlyOvertimeSummaryDto monthlyOvertimeSummaryDto;

    private DepartmentOvertimeSumDto departmentOvertimeSumDto;

    private DepartmentOvertimeAvgDto departmentOvertimeAvgDto;

}
