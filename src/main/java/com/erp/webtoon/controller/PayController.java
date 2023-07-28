package com.erp.webtoon.controller;

import com.erp.webtoon.dto.pay.PayRequestDto;
import com.erp.webtoon.dto.pay.PayResponseDto;
import com.erp.webtoon.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

    /**
     * 월 급여 등록
     */
    @PostMapping("/pays")
    public ResponseEntity save(@RequestBody PayRequestDto dto) {
        payService.save(dto);
        return new ResponseEntity(redirect(), HttpStatus.MOVED_PERMANENTLY);
    }

    private HttpHeaders redirect() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/pays/{employeeId}"));
        return headers;
    }
}
