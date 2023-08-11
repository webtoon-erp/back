package com.erp.webtoon.controller;

import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.user.UserRequestDto;
import com.erp.webtoon.fixture.UserFixture;
import com.erp.webtoon.repository.UserRepository;
import com.erp.webtoon.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @Test
    void add() throws Exception {
        // given
        User user = userRepository.save(UserFixture.FIRST_USER.생성(1L));

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .employeeId(user.getEmployeeId())
                .password(user.getPassword())
                .name(user.getName())
                .email(user.getEmail())
                .tel(user.getTel())
                .birthDate(user.getBirthDate())
                .deptName(user.getDeptName())
                .deptCode(user.getDeptCode())
                .teamNum(user.getTeamNum())
                .position(user.getPosition())
                .joinDate(user.getJoinDate())
                .dayOff(user.getDayOff())
                .build();

        String json = objectMapper.writeValueAsString(userRequestDto);

        // expected
        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}