package com.erp.webtoon.dto.token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogoutResponseDto {
    private Long employeeId;
}
