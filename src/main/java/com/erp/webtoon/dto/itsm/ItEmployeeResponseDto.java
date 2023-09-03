package com.erp.webtoon.dto.itsm;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ItEmployeeResponseDto {
    private String name;
    private String deptName;
    private String position;
    private String employeeId;
}
