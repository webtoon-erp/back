package com.erp.webtoon.dto.notice;

import com.erp.webtoon.domain.Notice;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoticeListDto {
    private String noticeType;

    private String title;

}
