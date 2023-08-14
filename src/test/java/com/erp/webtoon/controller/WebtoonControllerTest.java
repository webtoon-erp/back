package com.erp.webtoon.controller;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.domain.Webtoon;
import com.erp.webtoon.dto.webtoon.WebtoonRequestDto;
import com.erp.webtoon.repository.WebtoonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    @DisplayName("웹툰 등록")
    void test1() throws Exception{
        //given
        WebtoonRequestDto dto = new WebtoonRequestDto();
        dto.setTitle("제목입니다.");
        dto.setIntro("인트로입니다.");
        dto.setArtist("작가입니다.");

        //expected
        mockMvc.perform(post("/webtoon")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
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
                .andExpect(jsonPath("$[0].id").value(7L))
                .andExpect(jsonPath("$[0].title").value("제목입니다.7"))
                .andDo(print());
    }

    @Test
    @DisplayName("웹툰 개별 조회")
    void test3() throws Exception {
        //given
        Webtoon newWebtoon = Webtoon.builder()
                .title("제목입니다.")
                .intro("인트로입니다.")
                .category("월요일")
                .build();

        File file = File.builder()
                .originName("파일명")
                .ext("png")
                .build();

        file.updateFileWebtoon(newWebtoon);
        newWebtoon.getFiles().add(file);

        webtoonRepository.save(newWebtoon);

        //expected
        mockMvc.perform(get("/webtoon/{webtoonId}", newWebtoon.getId())
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목입니다."))
                .andExpect(jsonPath("$.resource").value("file : 파일명"))
                .andDo(print());
    }

}