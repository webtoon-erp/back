package com.erp.webtoon.dto.attendance;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TotalAttendanceResponseDto {

    private TotalAttendanceSummaryDto totalAttendanceSummaryDto;

    private MonthlyOvertimeSummaryDto monthlyOvertimeSummaryDto;

    private DepartmentOvertimeSumDto departmentOvertimeSumDto;

    private DepartmentOvertimeAvgDto departmentOvertimeAvgDto;

}
