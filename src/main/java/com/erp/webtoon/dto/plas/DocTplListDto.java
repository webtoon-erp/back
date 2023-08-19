package com.erp.webtoon.dto.plas;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocTplListDto {

    private Long id;

    private String templateName;    // 템플릿 이름

    private String intro; // 설명

    private String template;

}
