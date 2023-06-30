package com.erp.webtoon.service;

import com.erp.webtoon.domain.Plan;
import com.erp.webtoon.dto.plan.PlanRequestDto;
import com.erp.webtoon.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {

    private final PlanRepository planRepository;

    /**
     * 새로운 일정 등록
     */
    public Long save(PlanRequestDto dto) {
        Plan newPlan = dto.toEntity();

        planRepository.save(newPlan);

        return newPlan.getId();
    }

    /**
     * 일정 전체 조회
     */

    /**
     * 일정 개별 조회
     */

    /**
     * 일정 삭제
     */
    public void delete(Long planId) {
        Plan findPlan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 계획입니다."));

        planRepository.delete(findPlan);
    }
}
