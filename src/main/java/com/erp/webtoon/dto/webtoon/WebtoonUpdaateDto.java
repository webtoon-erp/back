package com.erp.webtoon.dto.webtoon;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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

    @NotBlank
    private String category;    //업로드 요일

    @NotBlank
    private String keyword; //키워드

    //private MultipartFile uploadFile;   //썸네일 파일
}
