package com.erp.webtoon.service;

import com.erp.webtoon.domain.Plan;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.plan.PlanRequestDto;
import com.erp.webtoon.repository.PlanRepository;
import com.erp.webtoon.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PlanServiceTest {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanService planService;

    @Test
    @Transactional
    @DisplayName("새로운 일정 등록")
    void save() {
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

        //when
        Long id = planService.save(requestDto);

        //then
        assertEquals(1L, planRepository.count());
        Plan plan = planRepository.findAll().get(0);
        assertEquals("규규", plan.getUser().getName());
    }

}