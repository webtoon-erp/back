package com.erp.webtoon.dto.itsm;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class RequestDto {
    private String reqType;

    private String title;

    private String content;

    private int step;

    private LocalDate dueDate;     // 기한 일자

    private LocalDate doneDate;    // 완료 일자

    private String reqUserId;   // 요청자 사번

    private String itUserId;    // 담당자 사번

    private List<RequestDtDto> requestDts;
}
