package com.erp.webtoon.dto.plas;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DocumentFileResponseDto {

    private Long id;

    private String originName;

}
