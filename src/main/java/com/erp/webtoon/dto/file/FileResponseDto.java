package com.erp.webtoon.dto.file;

import com.erp.webtoon.domain.File;
import lombok.Data;

@Data
public class FileResponseDto {

    private Long fileId;

    private String fileOriginName;

    private String filePath;

    public FileResponseDto(File file) {
        this.fileId = file.getId();
        this.fileOriginName = file.getOriginName();
        this.filePath = file.getFileName();
    }
}
