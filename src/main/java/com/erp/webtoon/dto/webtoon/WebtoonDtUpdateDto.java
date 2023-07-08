package com.erp.webtoon.dto.webtoon;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
public class WebtoonDtUpdateDto {

    @NotBlank
    private String episodeNum;

    @NotBlank
    private String subTitle;

    private MultipartFile uploadFile;
}
