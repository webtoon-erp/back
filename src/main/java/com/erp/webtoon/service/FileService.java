package com.erp.webtoon.service;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.dto.file.FileSaveDto;
import com.erp.webtoon.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;

    /**
     * 파일 등록
     */
    public File save(MultipartFile file) throws IOException {
        File newFile = new FileSaveDto().toEntity(file);
        fileRepository.save(newFile);

        return newFile;
    }

    /**
     * 파일 상태 변경 -> Y/N
     */
    public void changeStat(File file) {
        file.changeStat();
    }

}
