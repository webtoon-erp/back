package com.erp.webtoon.dto.notice;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class NoticeUpdateDto {

    @NotBlank
    private String noticeType;
    @NotBlank
    private String title;
    @NotBlank
    private String  content;
}
