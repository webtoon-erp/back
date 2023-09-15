package com.erp.webtoon.dto.notice;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NoticeUpdateDto {

    @NotBlank
    private String noticeType;
    @NotBlank
    private String title;
    @NotBlank
    private String  content;
}
