package com.erp.webtoon.controller;

import com.erp.webtoon.dto.pay.PayResponseDto;
import com.erp.webtoon.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    /**
     * 개인 급여 조회
     */
    @GetMapping("/pays/{employeeId}")
    public ResponseEntity show(@PathVariable String employeeId) {
        PayResponseDto dto = payService.search(employeeId);

        return ResponseEntity.ok(dto);
    }
}
