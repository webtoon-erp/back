package com.erp.webtoon.dto.user;

import com.erp.webtoon.domain.Qualification;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class QualificationResponseDto {

    private String qlfcType;

    private String content;

    private LocalDate qlfcDate;

    public static QualificationResponseDto from(Qualification qualification) {
        return QualificationResponseDto.builder()
                .qlfcType(qualification.getQlfcType())
                .content(qualification.getContent())
                .qlfcDate(qualification.getQlfcDate())
                .build();
    }
}
