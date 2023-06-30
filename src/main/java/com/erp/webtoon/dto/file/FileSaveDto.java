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
@NoArgsConstructor
public class FileSaveDto {
    /**
     * 파일 저장시 쓰이는 양식
     */

    @Value("${file.dir}")
    private String fileDir;

    private String filePath;

    private String originName;

    private String ext; // 파일 확장자

    private Long fileSize;

    // File Entity로 변환
    public File toEntity(MultipartFile file) throws IOException {
        return File.builder()
                .fileName(saveFile(file))
                .originName(file.getOriginalFilename())
                .ext(extractExt(file.getOriginalFilename()))
                .fileSize(file.getSize())
                .build();
    }


    // 파일 서버에 저장
    private String saveFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String storeFileName = createFileName(originalFilename);
        file.transferTo(new java.io.File(getFullPath(storeFileName)));
        return storeFileName;
    }

    // UUID 이용 파일 이름 반환
    private String createFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 파일 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    //전체 경로
    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

}
