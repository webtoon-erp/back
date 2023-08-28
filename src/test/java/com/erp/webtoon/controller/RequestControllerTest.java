package com.erp.webtoon.controller;

import com.erp.webtoon.domain.Request;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.itsm.RequestResponseDto;
import com.erp.webtoon.repository.RequestRepository;
import com.erp.webtoon.repository.UserRepository;
import com.erp.webtoon.service.RequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("요청 조회 기능")
    void test1() throws Exception {
        //given
        User user1 = User.builder()
                .employeeId("1")
                .deptName("IT")
                .name("규규")
                .build();

        User user2 = User.builder()
                .employeeId("2")
                .deptName("insa")
                .name("현현")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        Request request = Request.builder()
                .reqType("구매")
                .title("제목")
                .content("내용")
                .step(1)
                .reqUser(user2)
                .itUser(user1)
                .build();
        Request request1 = requestRepository.save(request);

        //expected
        mockMvc.perform(get("/request/{requestId}", request1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andDo(print());
    }

}