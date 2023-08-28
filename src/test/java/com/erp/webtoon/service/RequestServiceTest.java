package com.erp.webtoon.service;

import com.erp.webtoon.domain.Request;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.itsm.RequestResponseDto;
import com.erp.webtoon.repository.RequestRepository;
import com.erp.webtoon.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RequestServiceTest {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("요청 조회 기능")
    void test1() {
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

        //when
        RequestResponseDto responseDto = requestService.search(request1.getId());

        //then
        Assertions.assertEquals("구매", responseDto.getReqType());
        Assertions.assertEquals("제목", responseDto.getTitle());
        Assertions.assertEquals("내용", responseDto.getContent());
        Assertions.assertEquals(1, responseDto.getStep());
        Assertions.assertEquals("2", responseDto.getReqUserId());
        Assertions.assertEquals("1", responseDto.getItUserId());
    }

}