package com.erp.webtoon.dto.file;

import lombok.Data;

@Data
public class FileSaveDto {
    /**
     * 파일 저장시 쓰이는 양식
     */

    private String filePath;

    private String originName;

    private String ext; // 파일 확장자

    private Long fileSize;

}
