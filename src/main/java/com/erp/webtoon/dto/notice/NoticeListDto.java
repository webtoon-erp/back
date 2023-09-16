package com.erp.webtoon.dto.notice;

import com.erp.webtoon.domain.Notice;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NoticeListDto {
    private String deptName; // 부서명

    private String title;

    private String noticeType;

    private LocalDate noticeDate; // 작성일

    private String name; // 작성자

    private int readCount;  //조회수

    public NoticeListDto(Notice notice) {
        this.deptName = notice.getUser().getDeptName();
        this.title = notice.getTitle();
        this.noticeType = notice.getNoticeType();
        this.noticeDate = notice.getNoticeDate();
        this.name = notice.getUser().getName();
        this.readCount = notice.getReadCount();
    }
}
