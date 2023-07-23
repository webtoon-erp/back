package com.erp.webtoon.dto.plas;

import com.erp.webtoon.domain.User;
import lombok.Builder;
import lombok.Data;


import java.util.List;

@Data
@Builder
public class DocListDto {

    private String templateName; // 문서종류

    private String title; // 제목

    private char stat;  // 상태

    private String writeUsername;   // 작성자

    private List<String> documentRcvNames; // 수신자 목록

}
