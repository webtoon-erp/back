package com.erp.webtoon.controller;

import com.erp.webtoon.dto.plan.PlanRequestDto;
import com.erp.webtoon.dto.plan.PlanResponseDto;
import com.erp.webtoon.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
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
     * 일정조회(현재 월)
     * today 버튼 클릭 시 + 메인화면
     */
    @GetMapping("/plans")
    public ResponseEntity show() {
        int month = LocalDate.now().getMonthValue();
        List<PlanResponseDto> monthPlans = planService.getMonthPlans(month);

        return ResponseEntity.ok(monthPlans);
    }

    /**
     * 원하는 달 일정 조회
     */
    @GetMapping("/plans/{month}")
    public ResponseEntity showByMonth(@PathVariable("month") int month) {
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
