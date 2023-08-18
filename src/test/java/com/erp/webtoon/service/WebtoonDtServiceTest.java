package com.erp.webtoon.service;

import com.erp.webtoon.domain.User;
import com.erp.webtoon.domain.Webtoon;
import com.erp.webtoon.domain.WebtoonDt;
import com.erp.webtoon.dto.webtoon.WebtoonDtRequestDto;
import com.erp.webtoon.dto.webtoon.WebtoonDtResponseDto;
import com.erp.webtoon.dto.webtoon.WebtoonDtUpdateDto;
import com.erp.webtoon.repository.UserRepository;
import com.erp.webtoon.repository.WebtoonDtRepository;
import com.erp.webtoon.repository.WebtoonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class WebtoonDtServiceTest {

    @Autowired
    private WebtoonRepository webtoonRepository;

    @Autowired
    private WebtoonDtService webtoonDtService;

    @Autowired
    private WebtoonDtRepository webtoonDtRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {webtoonDtRepository.deleteAll();}

    public MockMultipartFile getMultipartFile() throws IOException {
        return new MockMultipartFile("file", "test.png", "image/png", new FileInputStream("/Users/kh/Desktop/file/파일명.png"));
    }

    @Test
    @DisplayName("웹툰 회차 임시 업로드(최초 등록) - 파일O")
    void test1() throws IOException {
        //given
        User newUser = User.builder()
                .employeeId("20232023")
                .name("규규")
                .position("과장")
                .build();

        userRepository.save(newUser);

        Webtoon newWebtoon = Webtoon.builder()
                .title("웹툰입니다.")
                .intro("인트로입니다.")
                .artist("작가입니다.")
                .build();

        webtoonRepository.save(newWebtoon);

        WebtoonDtRequestDto requestDto = new WebtoonDtRequestDto();
        requestDto.setWebtoonId(newWebtoon.getId());
        requestDto.setSubTitle("세상에 이런일이");
        requestDto.setEmployeeId("20232023");

        //when
        webtoonDtService.upload(requestDto, getMultipartFile());

        //then
        assertEquals(1L, webtoonDtRepository.count());
        WebtoonDt webtoonDt = webtoonDtRepository.findAll().get(0);
        assertEquals(1, webtoonDt.getEpisodeNum());
        assertEquals("세상에 이런일이", webtoonDt.getSubTitle());
        assertEquals("규규", webtoonDt.getManager());
    }

    @Test
    @DisplayName("웹툰 회차 최종 업로드")
    void test2() throws IOException {
        //given
        WebtoonDt newWebtoonDt = WebtoonDt.builder()
                .subTitle("세상에 이런일이")
                .content("감사합니다.")
                .build();

        webtoonDtRepository.save(newWebtoonDt);

        //when
        webtoonDtService.finalUpload(newWebtoonDt.getId());

        //then
        assertEquals(1L, webtoonDtRepository.count());
        WebtoonDt webtoonDt = webtoonDtRepository.findAll().get(0);
        assertEquals("세상에 이런일이", webtoonDt.getSubTitle());
        assertEquals(true, webtoonDt.isFinalUploadYN());
    }

    @Test
    @DisplayName("웹툰 회차 개별 조회 - 피드백 x")
    void test3() throws IOException {
        //given
        User newUser = User.builder()
                .employeeId("20232023")
                .name("규규")
                .position("과장")
                .build();

        userRepository.save(newUser);

        Webtoon newWebtoon = Webtoon.builder()
                .title("웹툰입니다.")
                .intro("인트로입니다.")
                .artist("작가입니다.")
                .build();

        webtoonRepository.save(newWebtoon);

        WebtoonDtRequestDto requestDto = new WebtoonDtRequestDto();
        requestDto.setWebtoonId(newWebtoon.getId());
        requestDto.setSubTitle("세상에 이런일이");
        requestDto.setEmployeeId("20232023");

        webtoonDtService.upload(requestDto, getMultipartFile());

        //when
        WebtoonDtResponseDto dto = webtoonDtService.showOne(webtoonDtRepository.findAll().get(0).getId());

        //then
        assertEquals(1L, webtoonDtRepository.count());
        assertEquals(1, dto.getEpisodeNum());
        assertEquals("세상에 이런일이", dto.getSubTitle());
        assertEquals(null, dto.getContent());
        assertEquals(new ArrayList<>(), dto.getFeedbackList());
    }

    @Test
    @DisplayName("웹툰 회차 수정 - 파일 x")
    void test4() throws IOException {
        //given
        User newUser = User.builder()
                .employeeId("20232023")
                .name("규규")
                .position("과장")
                .build();

        userRepository.save(newUser);

        WebtoonDt newWebtoonDt = WebtoonDt.builder()
                .subTitle("세상에 이런일이")
                .content("감사합니다.")
                .build();

        webtoonDtRepository.save(newWebtoonDt);

        WebtoonDtUpdateDto updateDto = new WebtoonDtUpdateDto();
        updateDto.setSubTitle("세상에 저런일이");
        updateDto.setContent("감사합니다.");
        updateDto.setManagerId("20232023");

        //when
        webtoonDtService.update(newWebtoonDt.getId(), updateDto, null);

        //then
        assertEquals(1L, webtoonDtRepository.count());
        WebtoonDt webtoonDt = webtoonDtRepository.findAll().get(0);
        assertEquals("세상에 저런일이", webtoonDt.getSubTitle());
        assertEquals("감사합니다.", webtoonDt.getContent());
        assertEquals("규규", webtoonDt.getManager());
        assertEquals(false, webtoonDt.isFinalUploadYN());
    }

}