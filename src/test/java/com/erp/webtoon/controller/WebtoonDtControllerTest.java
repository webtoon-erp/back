package com.erp.webtoon.controller;

import com.erp.webtoon.domain.User;
import com.erp.webtoon.domain.Webtoon;
import com.erp.webtoon.domain.WebtoonDt;
import com.erp.webtoon.dto.webtoon.WebtoonDtRequestDto;
import com.erp.webtoon.repository.UserRepository;
import com.erp.webtoon.repository.WebtoonDtRepository;
import com.erp.webtoon.repository.WebtoonRepository;
import com.erp.webtoon.service.WebtoonDtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WebtoonDtControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebtoonRepository webtoonRepository;

    @Autowired
    private WebtoonDtService webtoonDtService;

    @Autowired
    private WebtoonDtRepository webtoonDtRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean() {webtoonDtRepository.deleteAll();}

    public MockMultipartFile getMultipartFile() throws IOException {
        return new MockMultipartFile("file", "test.png", "image/png", new FileInputStream("/Users/kh/Desktop/file/파일명.png"));
    }

    @Test
    @DisplayName("웹툰 회차 임시 업로드(최초 등록) - 파일O")
    void test1() throws Exception {
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

        MockMultipartFile dto = new MockMultipartFile("dto", "dto", "application/json", objectMapper.writeValueAsString(requestDto).getBytes(StandardCharsets.UTF_8));

        //expected
        mockMvc.perform(multipart("/webtoonDt")
                .file(dto)
                .file(getMultipartFile()))
                .andExpect(status().isMovedPermanently())
                .andDo(print());

    }

    @Test
    @DisplayName("웹툰 회차 최종 업로드")
    void test2() throws Exception {
        //given
        WebtoonDt newWebtoonDt = WebtoonDt.builder()
                .subTitle("세상에 이런일이")
                .content("감사합니다.")
                .build();

        webtoonDtRepository.save(newWebtoonDt);

        //expected
        mockMvc.perform(post("/webtoonDt/{webtoonDtId}", newWebtoonDt.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

}