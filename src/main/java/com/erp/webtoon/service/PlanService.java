package com.erp.webtoon.service;

import com.erp.webtoon.domain.Plan;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.plan.PlanListDto;
import com.erp.webtoon.dto.plan.PlanRequestDto;
import com.erp.webtoon.dto.plan.PlanResponseDto;
import com.erp.webtoon.dto.plan.PlanUpdateDto;
import com.erp.webtoon.repository.PlanRepository;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {

    private final PlanRepository planRepository;

    private final UserRepository userRepository;

    /**
     * 새로운 일정 등록
     */
    @Transactional
    public Long save(PlanRequestDto dto) {
        User user = userRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 직원입니다."));

        Plan newPlan = dto.toEntity();
        newPlan.regisUser(user);

        Plan savePlan = planRepository.save(newPlan);

        return savePlan.getId();
    }

    /**
     * 일정 전체 조회 -> 홈 화면 시 보이는 일
     */
    public List<PlanListDto> getHomePlans() {
        List<Plan> plans = new ArrayList<>();
        plans.addAll(planRepository.findAll(Sort.by("month")));

        List<PlanListDto> responseDtoList = new ArrayList<>();
        if (!plans.isEmpty()) {
            responseDtoList.addAll(plans.stream()
                    .map(PlanListDto::new)
                    .collect(Collectors.toList()));
        }
        return responseDtoList;
    }


    /**
     * 일정 개별 조회
     */
    public PlanResponseDto getPlan(Long planId) {
        Plan findPlan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 일정입니다"));

        return new PlanResponseDto(findPlan);
    }

    /**
     * 일정 전체 조회
     */
    public List<PlanResponseDto> getAllPlan() {
        return planRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(PlanResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 일정 수정
     */
    @Transactional
    public void update(Long planId, PlanUpdateDto dto) {
        Plan findPlan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 일정입니다."));

        findPlan.updatePlan(dto.getPlanType(), dto.getTitle(), dto.getStartDate(),
                dto.getStartTime(), dto.getEndDate(), dto.getEndTime());
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
