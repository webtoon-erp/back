package com.erp.webtoon.dto.notice;

import com.erp.webtoon.domain.Notice;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class NoticeRequestDto {

    private String employeeId;

    private String noticeType;

    private String title;

    private String content;

    private List<MultipartFile> uploadFiles = new ArrayList<>();

    public Notice toEntity() {
        return Notice.builder()
                .noticeType(noticeType)
                .title(title)
                .content(content)
                .build();
    }
}
