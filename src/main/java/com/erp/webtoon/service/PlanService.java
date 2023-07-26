package com.erp.webtoon.service;

import com.erp.webtoon.domain.Plan;
import com.erp.webtoon.dto.plan.PlanRequestDto;
import com.erp.webtoon.dto.plan.PlanResponseDto;
import com.erp.webtoon.dto.plan.PlanUpdateDto;
import com.erp.webtoon.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {

    private final PlanRepository planRepository;

    /**
     * 새로운 일정 등록
     */
    @Transactional
    public Long save(PlanRequestDto dto) {
        Plan newPlan = dto.toEntity();

        planRepository.save(newPlan);

        return newPlan.getId();
    }

    /**
     * 일정 전체 조회 -> 원하는 월별로 가져와야 함
     */
    public List<PlanResponseDto> getMonthPlans(int month) {
        List<Plan> plans = planRepository.findByMonth(month);

        List<PlanResponseDto> dtos = new ArrayList<>();

        for (Plan plan : plans) {
            dtos.add(PlanResponseDto.builder()
                    .planType(plan.getPlanType())
                    .content(plan.getContent())
                    .startDate(plan.getStartDate())
                    .startTime(plan.getStartTime())
                    .endDate(plan.getEndDate())
                    .endTime(plan.getEndTime())
                    .holidayYN(plan.isHolidayYN())
                    .build());
        }

        return dtos;
    }


    /**
     * 일정 개별 조회 -> 일정 개별 클릭 시
     * 프론트에서 일별 계획의 ID를 가지고 있을 수 있나? -> 물어보기
     */
    public PlanResponseDto getPlan(Long planId) {
        Plan findPlan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 계획입니다"));

        return PlanResponseDto.builder()
                .planType(findPlan.getPlanType())
                .content(findPlan.getContent())
                .startDate(findPlan.getStartDate())
                .startTime(findPlan.getStartTime())
                .endDate(findPlan.getEndDate())
                .endTime(findPlan.getEndTime())
                .holidayYN(findPlan.isHolidayYN())
                .build();
    }

    /**
     * 일정 수정
     */
    @Transactional
    public void update(Long planId, PlanUpdateDto dto) {
        Plan findPlan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 일정입니다."));

        findPlan.updatePlan(dto.getPlanType(), dto.getContent(), LocalDate.of(dto.getStartYear(), dto.getStartMonth(), dto.getStartDay()),
                LocalTime.of(dto.getStartHour(), dto.getStartMinute()), LocalDate.of(dto.getEndYear(), dto.getEndMonth(), dto.getEndDay()),
                LocalTime.of(dto.getEndHour(), dto.getEndMinute()));

    }

    /**
     * 일정 삭제
     */
    @Transactional
    public void delete(Long planId) {
        Plan findPlan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 계획입니다."));

        planRepository.delete(findPlan);
    }
}
