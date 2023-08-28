package com.erp.webtoon.dto.file;

import com.erp.webtoon.domain.File;
import lombok.Builder;
import lombok.Data;

@Data
public class FileResponseDto {

    private Long fileId;

    private String fileOriginName;

    public FileResponseDto(File file) {
        this.fileId = file.getId();
        this.fileOriginName = file.getOriginName();
    }
}
