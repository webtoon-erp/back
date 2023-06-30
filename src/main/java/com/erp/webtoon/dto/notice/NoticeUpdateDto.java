package com.erp.webtoon.dto.notice;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class NoticeUpdateDto {

    Optional<String> noticeType;
    Optional<String> title;
    Optional<String> content;

    List<MultipartFile> uploadFiles = new ArrayList<>();

}
