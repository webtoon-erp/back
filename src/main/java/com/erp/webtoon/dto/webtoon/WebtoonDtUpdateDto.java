package com.erp.webtoon.dto.webtoon;

import lombok.Data;
import org.springframework.context.annotation.Primary;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
public class WebtoonDtUpdateDto {

    @NotBlank
    private String subTitle;

    @NotBlank
    private String content;

    @NotBlank
    private String managerId; // 담당자 사번

    private MultipartFile uploadFile;
}
