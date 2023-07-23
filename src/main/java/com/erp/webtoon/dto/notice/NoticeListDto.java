package com.erp.webtoon.dto.notice;

import com.erp.webtoon.domain.Notice;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NoticeListDto {
    private String noticeType;

    private String title;

    private LocalDate noticeDate;

    private int readCount;

    public NoticeListDto(Notice notice) {
        this.noticeType = notice.getNoticeType();
        this.title = notice.getTitle();
        this.noticeDate = notice.getNoticeDate();
        this.readCount = notice.getReadCount();
    }
}
