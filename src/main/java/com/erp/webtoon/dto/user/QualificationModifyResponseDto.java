package com.erp.webtoon.dto.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class QualificationModifyResponseDto {
    private Long qualificationId;

    private LocalDate modifiedDate;
}
