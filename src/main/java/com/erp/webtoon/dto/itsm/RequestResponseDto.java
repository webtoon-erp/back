package com.erp.webtoon.dto.itsm;

import com.erp.webtoon.dto.file.FileResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class RequestResponseDto {

    private String reqType;

    private String title;

    private String content;

    private int step; // 1: 요청 2: 접수 3: 진행 4: 완료 5: 반려

    private LocalDate dueDate;     // 기한 일자

    private LocalDate doneDate;    // 완료 일자

    private String reqUserId;   // 요청자 사번

    private String itUserId;    // 담당자 사번

    private List<FileResponseDto> files;

    private List<RequestDtResponseDto> requestDtList;
}
