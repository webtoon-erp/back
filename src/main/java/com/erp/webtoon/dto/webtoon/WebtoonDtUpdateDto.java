package com.erp.webtoon.dto.webtoon;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WebtoonDtUpdateDto {

    @NotBlank
    private String subTitle;

    @NotBlank
    private String content;

    @NotBlank
    private String managerId; // 담당자 사번

}
