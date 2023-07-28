package com.erp.webtoon.dto.notice;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class NoticeResponseDto {

    private String title;

    private String content;

    private int readCount;  // 조회수

    private String noticeType;

    private LocalDate noticeDate;

    private String name; // 작성자

    private List<String> originFileNames; // 오리지널 파일명

}
