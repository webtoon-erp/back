package com.erp.webtoon.service;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.repository.FileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileServiceTest {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileService fileService;

    @BeforeEach
    private void clean() {
        fileRepository.deleteAll();
    }

    private MockMultipartFile getMultipartFile() throws IOException {
        return new MockMultipartFile("test", "test.jpg", "image/jpg"
                                    ,new FileInputStream("/Users/kh/Desktop/file/파일명.png"));
    }

    @Test
    @DisplayName("파일 저장")
    void test1() throws IOException {
        //given, when
        File newFile = fileService.save(getMultipartFile());

        //then
        assertEquals(1L, fileRepository.count());
        File file = fileRepository.findAll().get(0);
        System.out.println(file.getFileName());
        assertEquals("test.jpg", file.getOriginName());
        assertEquals("jpg", file.getExt());
    }

    @Test
    @DisplayName("파일 상테 변경")
    void test2() {
        //given
        File newFile = File.builder()
                .originName("test.png")
                .build();

        fileRepository.save(newFile);

        //when
        fileService.changeStat(newFile.getId());

        //then
        assertEquals(1L, fileRepository.count());
        File file = fileRepository.findAll().get(0);
        assertEquals(false, file.isStat());
    }

    @Test
    @DisplayName("파일 개별 조회")
    void test3() {
        //given
        File newFile = File.builder()
                .originName("test.png")
                .ext("png")
                .fileSize(23333L)
                .build();

        fileRepository.save(newFile);

        //when
        File file = fileService.find(1L);

        //then
        assertEquals(1L, fileRepository.count());
        assertEquals(newFile.getId(), file.getId());
        assertEquals("test.png", file.getOriginName());
        assertEquals(23333L, file.getFileSize());
        assertEquals("png", file.getExt());
    }
}