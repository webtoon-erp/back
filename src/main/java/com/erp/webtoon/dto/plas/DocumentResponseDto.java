package com.erp.webtoon.dto.plas;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DocumentResponseDto {

    private String title;

    private String content;

    private LocalDateTime regDate;

    private String templateName;

    private String writeUserDeptName;

    private int writeUserTeamNum;

    private String writeUserPosition;

    private String writeUserEmployeeId;

    private String writeUserName;

    private List<DocumentFileResponseDto> uploadFiles;

    private List<DocumentRcvResponseDto> documentRcvResponses;

    private List<DocumentDataDto>  documentDataResponses;




}
