package com.erp.webtoon.service;

import com.erp.webtoon.domain.Plan;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.plan.PlanListDto;
import com.erp.webtoon.dto.plan.PlanRequestDto;
import com.erp.webtoon.dto.plan.PlanResponseDto;
import com.erp.webtoon.repository.PlanRepository;
import com.erp.webtoon.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Test
    @DisplayName("일정 전체 조회")
    void planList() {
        //given
        List<Plan> plans = IntStream.range(1, 25)
                .mapToObj(i -> Plan.builder()
                        .title("title" + i)
                        .month(i % 12).build())
                .collect(Collectors.toList());
        planRepository.saveAll(plans);

        //when
        List<PlanListDto> homePlans = planService.getHomePlans();

        //then
        assertEquals(24L, planRepository.count());
        assertEquals(24, homePlans.size());
        assertEquals("title12", homePlans.get(0).getTitle());
        assertEquals("title24", homePlans.get(1).getTitle());
    }

    @Test
    @DisplayName("일정 개별 조회")
    void search() {
        //given
        User user = User.builder()
                .employeeId("1")
                .name("규규")
                .deptName("인사")
                .build();
        userRepository.save(user);

        Plan plan = Plan.builder()
                .title("코딩")
                .startDate(LocalDate.now())
                .startTime(LocalTime.now())
                .endDate(LocalDate.of(2023, 10, 12))
                .endTime(LocalTime.of(13, 00))
                .build();

        plan.regisUser(user);
        Plan savePlan = planRepository.save(plan);

        //when
        PlanResponseDto responseDto = planService.getPlan(savePlan.getId());

        //then
        assertEquals("코딩", responseDto.getTitle());
        assertEquals("규규", responseDto.getName());
        assertEquals(LocalDate.now(), responseDto.getStartDate());
        assertEquals(LocalDate.of(2023, 10, 12), responseDto.getEndDate());
    }
}