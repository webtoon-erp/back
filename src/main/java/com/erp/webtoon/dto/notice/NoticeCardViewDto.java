package com.erp.webtoon.dto.notice;

import com.erp.webtoon.domain.Notice;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NoticeCardViewDto {

    private Long id;

    private String title;

    private String noticeType;

    private LocalDate noticeDate;

    public NoticeCardViewDto(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.noticeType = notice.getNoticeType();
        this.noticeDate = notice.getNoticeDate();
    }
}
