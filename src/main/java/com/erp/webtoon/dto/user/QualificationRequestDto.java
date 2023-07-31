package com.erp.webtoon.dto.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class QualificationRequestDto {
    private Integer sortSequence; // 정렬번호

    private String employeeId; // 사원 번호

    private String qlfcType;    // 자격증 타입 (자격증 이름)

    private String content;     // 내용 (자격증 상세)

    private LocalDate qlfcDate;    // 만료일자

    private int qlfcPay;    // 자격수당
}
