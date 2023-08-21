package com.erp.webtoon.dto.plas;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class DayOffDocumentRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @CreationTimestamp
    private LocalDateTime regDate;      // 작성일시

    @NotBlank
    private String templateName;

    @NotBlank
    private String writeEmployeeId;     // 작성자ID

    @NotBlank
    private LocalDateTime dayOffDate;   // 연차일자

}
