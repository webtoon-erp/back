package com.erp.webtoon.dto.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class QualificationResponseDto {
    private String qlfcType;
    private String content;
    private LocalDate qlfcDate;
}
