package com.erp.webtoon.controller;

import com.erp.webtoon.domain.Qualification;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.pay.PayQualificationDto;
import com.erp.webtoon.dto.pay.PayRequestDto;
import com.erp.webtoon.dto.pay.PayResponseDto;
import com.erp.webtoon.repository.UserRepository;
import com.erp.webtoon.service.PayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PayService payService;

    @Test
    @DisplayName("월 급여 등록")
    void test1() throws Exception {
        //given
        User user = User.builder()
                .employeeId("2000")
                .build();

        userRepository.save(user);

        PayRequestDto dto = new PayRequestDto();
        dto.setEmployeeId("2000");
        dto.setYearSalary(100000);
        dto.setAddSalary(20000);
        dto.setBankAccount("000-000-000-000");
        dto.setPayDate(LocalDate.now());

        String dtoToJson = objectMapper.writeValueAsString(dto);

        //expected
        mockMvc.perform(post("/pays")
                        .content(dtoToJson)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isMovedPermanently())
                .andDo(print());
    }

    @Test
    @DisplayName("개인 급여 상세 조회")
    void test2() throws Exception {
        //given
        User user = User.builder()
                .employeeId("2000")
                .name("규규")
                .email("kkk@naver.com")
                .deptName("인사과")
                .build();

        List<Qualification> qualList = new ArrayList<>();
        Qualification qualification = Qualification.builder()
                .qlfcType("정처기")
                .content(null)
                .qlfcDate(LocalDate.now())
                .qlfcPay(50000)
                .user(user)
                .build();
        qualList.add(qualification);
        user.registerQualification(qualList);

        userRepository.save(user);

        PayRequestDto dto = new PayRequestDto();
        dto.setEmployeeId("2000");
        dto.setYearSalary(100000);
        dto.setAddSalary(20000);
        dto.setBankAccount("000-000-000-000");
        dto.setPayDate(LocalDate.now());

        payService.save(dto);

        //expected
        mockMvc.perform(get("/pays/{employeeId}", "2000")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userInfo.name").value("규규"))
                .andExpect(jsonPath("$.userInfo.email").value("kkk@naver.com"))
                .andExpect(jsonPath("$.monthPay.bankAccount").value("000-000-000-000"))
                .andExpect(jsonPath("$.monthPay.monthSalary").value(8333))
                .andExpect(jsonPath("$.monthPay.qualSalary").value(50000))
                        .andDo(print());
    }
}