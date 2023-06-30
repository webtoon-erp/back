package com.erp.webtoon.service;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.dto.file.FileSaveDto;
import com.erp.webtoon.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {
    @Value("${file.dir}")
    private String fileDir;

    private final FileRepository fileRepository;

    /**
     * 파일 등록
     */
    public File save(MultipartFile file) throws IOException {

        File newFile = File.builder()
                .fileName(saveFile(file))
                .originName(file.getOriginalFilename())
                .ext(extractExt(file.getOriginalFilename()))
                .fileSize(file.getSize())
                .build();

        fileRepository.save(newFile);

        return newFile;
    }

    /**
     * 파일 상태 변경 -> Y/N
     */
    public void changeStat(File file) {
        file.changeStat();
    }

    /**
     * 파일 조회
     */
    public File find(Long fileId) {
        File findFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 파일입니다."));

        return findFile;
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
