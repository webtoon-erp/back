package com.erp.webtoon.controller;

import com.erp.webtoon.domain.Webtoon;
import com.erp.webtoon.dto.webtoon.WebtoonRequestDto;
import com.erp.webtoon.dto.webtoon.WebtoonUpdaateDto;
import com.erp.webtoon.repository.WebtoonRepository;
import com.erp.webtoon.service.FileService;
import com.erp.webtoon.service.WebtoonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebtoonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebtoonRepository webtoonRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private WebtoonService webtoonService;

    @BeforeEach
    void clean() {webtoonRepository.deleteAll();}

    private MockMultipartFile getMultipartFile() throws IOException {
        return new MockMultipartFile("file", "test.png", "image/png", new FileInputStream("/Users/kh/Desktop/file/파일명.png"));
    }

    @Test
    @DisplayName("웹툰 등록-파일o")
    void test1() throws Exception {
        //given
        WebtoonRequestDto requestDto = new WebtoonRequestDto();
        requestDto.setTitle("제목입니다.");
        requestDto.setIntro("인트로입니다.");
        requestDto.setArtist("작가입니다.");

        MockMultipartFile dto = new MockMultipartFile("dto", "dto", "application/json", objectMapper.writeValueAsString(requestDto).getBytes(StandardCharsets.UTF_8));

        //expected
        mockMvc.perform(multipart("/webtoon")
                        .file(getMultipartFile())
                        .file(dto))
                .andExpect(status().isMovedPermanently())
                .andExpect(jsonPath("$.id").value(webtoonRepository.findAll().get(0).getId()))
                .andDo(print());
    }

    @Test
    @DisplayName("웹툰 전체 리스트 조회")
    void test2() throws Exception {
        //given
        List<Webtoon> webtoonList = IntStream.range(1, 22)
                .mapToObj(i -> Webtoon.builder()
                        .title("제목입니다." + i)
                        .artist("작가입니다." + i)
                        .illustrator("그림 작가입니다." + i)
                        .category((i % 7) + "요일")
                        .keyword("카테고리" + i)
                        .build())
                .collect(Collectors.toList());

        webtoonRepository.saveAll(webtoonList);

        //expected
        mockMvc.perform(get("/webtoon")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(21)))
                .andExpect(jsonPath("$[0].title").value("제목입니다.7"))
                .andExpect(jsonPath("$[0].artist").value("작가입니다.7"))
                .andExpect(jsonPath("$[3].title").value("제목입니다.1"))
                .andExpect(jsonPath("$[3].artist").value("작가입니다.1"))
                .andDo(print());
    }

    @Test
    @DisplayName("웹툰 개별 조회")
    void test3() throws Exception {
        //given
        WebtoonRequestDto webtoonDto = new WebtoonRequestDto();
        webtoonDto.setTitle("제목입니다.");
        webtoonDto.setIntro("인트로입니다.");
        webtoonDto.setCategory("월요일");


        Long webtoonId = webtoonService.save(webtoonDto, getMultipartFile());


        //expected
        mockMvc.perform(get("/webtoon/{webtoonId}", webtoonId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("제목입니다."))
                .andExpect(jsonPath("$..info.intro").value("인트로입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("웹툰 수정-파일x")
    void test4() throws Exception {
        //given
        Webtoon newWebtoon = Webtoon.builder()
                .title("제목입니다.")
                .intro("인트로입니다.")
                .artist("규규")
                .illustrator("그림 작가")
                .category("월요일")
                .keyword("공포")
                .build();

        webtoonRepository.save(newWebtoon);

        WebtoonUpdaateDto updaateDto = new WebtoonUpdaateDto();
        updaateDto.setTitle("제목입니다.");
        updaateDto.setIntro("인트로입니다.");
        updaateDto.setArtist("규규2");
        updaateDto.setIllustrator("규규2");
        updaateDto.setCategory("월요일");
        updaateDto.setKeyword("공포");

        String updateDtoJson = objectMapper.writeValueAsString(updaateDto);
        MockMultipartFile dto = new MockMultipartFile("dto", "dto", "application/json", updateDtoJson.getBytes(StandardCharsets.UTF_8));

        //expected
        mockMvc.perform(multipart(HttpMethod.PUT,"/webtoon/{webtoonId}", newWebtoon.getId())
                        .file(dto))
                .andExpect(status().isMovedPermanently())
                .andDo(print());
    }

    @Test
    @DisplayName("웹툰 수정-파일o")
    void test5() throws Exception {
        //given
        WebtoonRequestDto webtoonDto = new WebtoonRequestDto();
        webtoonDto.setTitle("제목입니다.");
        webtoonDto.setIntro("인트로입니다.");
        webtoonDto.setArtist("작가");
        webtoonDto.setIllustrator("그림");
        webtoonDto.setCategory("월요일");
        webtoonDto.setKeyword("공포");


        Long webtoonId = webtoonService.save(webtoonDto, getMultipartFile());

        WebtoonUpdaateDto updaateDto = new WebtoonUpdaateDto();
        updaateDto.setTitle("제목입니다.");
        updaateDto.setIntro("인트로입니다.");
        updaateDto.setArtist("규규2");
        updaateDto.setIllustrator("규규2");
        updaateDto.setCategory("월요일");
        updaateDto.setKeyword("공포");

        String updateDtoJson = objectMapper.writeValueAsString(updaateDto);
        MockMultipartFile dto = new MockMultipartFile("dto", "dto", "application/json", updateDtoJson.getBytes(StandardCharsets.UTF_8));


        //expected
        mockMvc.perform(multipart(HttpMethod.PUT,"/webtoon/{webtoonId}", webtoonId)
                        .file(getMultipartFile())
                        .file(dto))
                .andExpect(status().isMovedPermanently())
                .andDo(print());
    }
}