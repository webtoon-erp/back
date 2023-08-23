package com.erp.webtoon.dto.common;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ErrorResponseDto {

    private String message;

    public ErrorResponseDto(String message) {
        this.message = message;
    }

}
