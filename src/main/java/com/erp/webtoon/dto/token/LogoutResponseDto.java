package com.erp.webtoon.dto.token;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LogoutResponseDto {
    private String employeeId;
}
