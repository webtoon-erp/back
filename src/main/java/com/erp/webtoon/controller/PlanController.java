package com.erp.webtoon.controller;

import com.erp.webtoon.dto.plan.PlanRequestDto;
import com.erp.webtoon.dto.plan.PlanResponseDto;
import com.erp.webtoon.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    /**
     * 일정등록
     */
    @PostMapping("/plans")
    public ResponseEntity save(@RequestBody PlanRequestDto dto) {
        Long planId =  planService.save(dto);
        return ResponseEntity.ok(planId);
    }

    /**
     * 일정조회(월별)
     */
    @GetMapping("/plans/{month}")
    public ResponseEntity show(@PathVariable("month") int month) {
        List<PlanResponseDto> monthPlans = planService.getMonthPlans(month);

        return ResponseEntity.ok(monthPlans);
    }


    /**
     * 일정삭제
     */
    @DeleteMapping("/plans/{planId}")
    public void delete(@PathVariable Long planId) {
        planService.delete(planId);
    }
}
