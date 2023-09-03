package com.erp.webtoon.dto.itsm;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class RequestDeleteDto {
    private Long requestId;

    private List<RequestDeleteDto> requestIds;

}
