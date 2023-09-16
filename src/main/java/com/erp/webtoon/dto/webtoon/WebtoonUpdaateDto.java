package com.erp.webtoon.dto.webtoon;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WebtoonUpdaateDto {

    @NotBlank
    private String title;

    @NotBlank
    private String intro; // 웹툰 소개

    @NotBlank
    private String artist;  //작가 이름

    @NotBlank
    private String illustrator; // 그림 작가 이름

}
