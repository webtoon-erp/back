package com.erp.webtoon.dto.itsm;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RequestDeleteDto {
    private Long requestId;

    private List<RequestDeleteDto> requestIds;

}
