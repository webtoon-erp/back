package com.erp.webtoon.controller;

import com.erp.webtoon.domain.Notice;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.notice.NoticeRequestDto;
import com.erp.webtoon.dto.notice.NoticeResponseDto;
import com.erp.webtoon.dto.notice.NoticeUpdateDto;
import com.erp.webtoon.repository.NoticeRepository;
import com.erp.webtoon.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NoticeControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("공지사항 등록")
    void test1() throws Exception{
        //given
        User user = User.builder()
                .employeeId("20232023")
                .build();

        userRepository.save(user);

        NoticeRequestDto noticeRequestDto = new NoticeRequestDto();
        noticeRequestDto.setEmployeeId("20232023");
        noticeRequestDto.setNoticeType("인사팀");
        noticeRequestDto.setTitle("제목입니다.");
        noticeRequestDto.setContent("내용입니다.");

        String json = objectMapper.writeValueAsString(noticeRequestDto);

        //expected
        mockMvc.perform(post("/notice")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isMovedPermanently())
                .andDo(print());
    }

    @Test
    @DisplayName("공지사항 개별 조회")
    void test2() throws Exception {
        //given
        User user = User.builder()
                .employeeId("20232023")
                .name("규규")
                .build();

        userRepository.save(user);

        Notice newNotice = Notice.builder()
                .noticeType("인사팀")
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        newNotice.setWriteUser(user);
        noticeRepository.save(newNotice);

        //expected
        mockMvc.perform(get("/notice/{noticeId}", newNotice.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목입니다."))
                .andExpect(jsonPath("$.content").value("내용입니다."))
                .andExpect(jsonPath("$.readCount").value(2))
                .andExpect(jsonPath("$.name").value("규규"))
                .andDo(print());
    }

    @Test
    @DisplayName("공지사항 전체 조회")
    void test3() throws Exception {
        //given
        User user = User.builder()
                .employeeId("20232023")
                .name("규규")
                .build();

        userRepository.save(user);

        List<Notice> notices = IntStream.range(1, 31)
                .mapToObj(i -> Notice.builder()
                        .noticeType("type" + i)
                        .title("title" + i)
                        .content("content" + i)
                        .build())
                .collect(Collectors.toList());

        for (Notice notice: notices) {
            notice.setWriteUser(user);
        }

        noticeRepository.saveAll(notices);

        //expected
        mockMvc.perform(get("/notice")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(30)))
                .andExpect(jsonPath("$[0].title").value("title30"))
                .andExpect(jsonPath("$[0].name").value("규규"))
                .andExpect(jsonPath("$[29].title").value("title1"))
                .andExpect(jsonPath("$[29].name").value("규규"))
                .andDo(print());
    }

    @Test
    @DisplayName("공지사항 카드뷰 조회")
    void test4() throws Exception {
        //given
        List<Notice> notices = IntStream.range(1, 31)
                .mapToObj(i -> Notice.builder()
                        .noticeType("type" + i)
                        .title("title" + i)
                        .content("content" + i)
                        .build())
                .collect(Collectors.toList());

        noticeRepository.saveAll(notices);

        //expected
        mockMvc.perform(get("/home")
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("공지사항 수정")
    void test5() throws Exception {
        //given
        User user = User.builder()
                .employeeId("20232023")
                .name("규규")
                .build();

        userRepository.save(user);

        Notice newNotice = Notice.builder()
                .noticeType("인사")
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        newNotice.setWriteUser(user);
        noticeRepository.save(newNotice);

        NoticeUpdateDto updateDto = new NoticeUpdateDto();
        updateDto.setNoticeType("인사부");
        updateDto.setTitle("제목입니다.");
        updateDto.setContent("내용입니다2.");

        //expected
        mockMvc.perform(put("/notice/{noticeId}", newNotice.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isMovedPermanently())
                .andDo(print());

        mockMvc.perform(get("/notice/{noticeId}", newNotice.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목입니다."))
                .andExpect(jsonPath("$.content").value("내용입니다2."))
                .andExpect(jsonPath("$.noticeType").value("인사부"))
                .andExpect(jsonPath("$.readCount").value(2))
                .andExpect(jsonPath("$.name").value("규규"))
                .andDo(print());
    }

    @Test
    @DisplayName("공지사항 삭제")
    void test6() throws Exception {
        //given
        Notice newNotice = Notice.builder()
                .noticeType("인사")
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        noticeRepository.save(newNotice);

        //expected
        mockMvc.perform(delete("/notice/{noticeId}", newNotice.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isMovedPermanently())
                .andDo(print());
    }
}