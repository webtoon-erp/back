package com.erp.webtoon.controller;

import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.plan.PlanRequestDto;
import com.erp.webtoon.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("일정등록")
    void save() throws Exception {
        //given
        User user = User.builder()
                .employeeId("1")
                .name("규규")
                .deptName("인사")
                .build();
        userRepository.save(user);

        PlanRequestDto requestDto = new PlanRequestDto();
        requestDto.setEmployeeId("1");
        requestDto.setStartDate(LocalDate.now());
        requestDto.setTitle("나들이");

        //expected
        mockMvc.perform(post("/plans")
                        .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

}