package com.erp.webtoon.dto.notice;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NoticeUpdateDto {


    private String noticeType;

    private String title;

    private String  content;
}
