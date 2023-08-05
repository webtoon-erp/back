package com.erp.webtoon.dto.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class RegisterQualificationResponse {
    private Long QualificationId;
    private LocalDate createdAt;
}
