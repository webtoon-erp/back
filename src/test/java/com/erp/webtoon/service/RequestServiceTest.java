package com.erp.webtoon.service;

import com.erp.webtoon.domain.Request;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.itsm.RequestListResponseDto;
import com.erp.webtoon.dto.itsm.RequestResponseDto;
import com.erp.webtoon.repository.RequestRepository;
import com.erp.webtoon.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Test
    @DisplayName("사원별 개인 요청 리스트 조회 기능")
    void test2() {
        //given
        User user1 = User.builder()
                .employeeId("1")
                .deptName("IT")
                .name("규규")
                .build();
        userRepository.save(user1);

        List<Request> requests = IntStream.range(1, 11)
                .mapToObj(i -> Request.builder()
                        .reqType("구매" + i)
                        .title("제목" + i)
                        .content("내용" + i)
                        .step(1)
                        .reqUser(user1)
                        .itUser(user1)
                        .build())
                .collect(Collectors.toList());
        for (Request request : requests) {
            request.updateUserRequest();
        }
        requestRepository.saveAll(requests);

        //when
        List<RequestListResponseDto> dtos = requestService.searchUserList(user1.getEmployeeId());

        //then
        Assertions.assertEquals(10, requestRepository.count());
        RequestListResponseDto dto = dtos.get(1);
        Assertions.assertEquals("구매2", dto.getReqType());
        Assertions.assertEquals("제목2", dto.getTitle());
        Assertions.assertEquals(1, dto.getStep());
        Assertions.assertEquals("1", dto.getReqUser());
    }

    @Test
    @DisplayName("IT팀 과거 요청 전체 리스트 조회")
    void test3() throws IllegalAccessException {
        //given
        User user1 = User.builder()
                .employeeId("1")
                .deptName("IT")
                .name("규규")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .employeeId("2")
                .deptName("인사")
                .name("현현")
                .build();
        userRepository.save(user2);

        List<Request> requests = IntStream.range(1, 11)
                .mapToObj(i -> Request.builder()
                        .reqType("구매" + i)
                        .title("제목" + i)
                        .content("내용" + i)
                        .step(1)
                        .reqUser(user2)
                        .itUser(user1)
                        .build())
                .collect(Collectors.toList());
        for (Request request : requests) {
            request.updateUserRequest();
        }
        requestRepository.saveAll(requests);

        //when
        List<RequestListResponseDto> responseDtos = requestService.searchAllList(user1.getEmployeeId());

        //then
        Assertions.assertEquals(10, requestRepository.count());
        RequestListResponseDto dto = responseDtos.get(1);
        Assertions.assertEquals("구매2", dto.getReqType());
        Assertions.assertEquals("제목2", dto.getTitle());
        Assertions.assertEquals(1, dto.getStep());
        Assertions.assertEquals("2", dto.getReqUser());
    }
}