package com.erp.webtoon.dto.plas;

import com.erp.webtoon.domain.User;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DocListDto {

    private String templateName; // 문서종류

    private LocalDateTime reg_date; // 작성일시

    private String title; // 제목

    private String writeDeptName; // 작성부서

    private String writeUsername;   // 작성자

    private List<String> documentRcvNames; // 수신자 목록

    private char stat;  // 상태

}
