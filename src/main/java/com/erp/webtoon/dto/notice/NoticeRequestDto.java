package com.erp.webtoon.dto.notice;

import com.erp.webtoon.domain.Notice;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class NoticeRequestDto {

    private String noticeType;

    private String title;

    private String content;

    private List<MultipartFile> uploadFiles;

    public Notice toEntity() {
        return Notice.builder()
                .noticeType(noticeType)
                .title(title)
                .content(content)
                .build();
    }
}
