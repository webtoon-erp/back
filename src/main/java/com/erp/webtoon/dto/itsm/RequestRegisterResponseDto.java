package com.erp.webtoon.dto.itsm;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class RequestRegisterResponseDto {
    private Long requestId;

    private LocalDateTime createdAt;
}
