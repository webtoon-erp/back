package com.erp.webtoon.dto.plas;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class DocListDto {

    private Long id;

    private String templateName; // 문서종류

    private LocalDateTime reg_date; // 작성일시

    private String title; // 제목

    private String writeDeptName; // 작성부서

    private String writeUsername;   // 작성자

    private String currentApprover; // 결재대기자

    private String lastApprover; // 최종결재자

    private char stat;  // 상태

}
