package com.erp.webtoon.dto.itsm;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestDeleteDto {
    private Long requestId;
}
