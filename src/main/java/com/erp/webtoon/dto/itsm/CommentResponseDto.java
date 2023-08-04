package com.erp.webtoon.dto.itsm;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class CommentResponseDto {
    private Long requestId;
    private LocalDate createdAt;
}
