package com.erp.webtoon.dto.notice;

import com.erp.webtoon.domain.Notice;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NoticeRequestDto {

    @NotBlank
    private String employeeId;

    @NotBlank
    private String noticeType;

    @NotBlank
    private String deptName;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    public Notice toEntity() {
        return Notice.builder()
                .noticeType(noticeType)
                .title(title)
                .content(content)
                .build();
    }
}
