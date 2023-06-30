package com.erp.webtoon.dto.notice;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NoticeResponseDto {
    private String noticeType;

    private String title;

    private String content;

    private int readCount;  // 조회수

    private List<String> originFileNames; // 오리지널 파일명

}
