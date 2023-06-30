package com.erp.webtoon.dto.file;

import com.erp.webtoon.domain.File;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
