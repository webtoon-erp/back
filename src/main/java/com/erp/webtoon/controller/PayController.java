package com.erp.webtoon.controller;

import com.erp.webtoon.dto.pay.*;
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

    /**
     * 월 급여 수정
     */
    @PutMapping("/pays/{employeeId}")
    public ResponseEntity update(@PathVariable String employeeId, @RequestBody PayMonthUpdateDto dto) {
        payService.updateMonthPay(employeeId, dto);
        return new ResponseEntity(redirect(), HttpStatus.MOVED_PERMANENTLY);
    }

    /**
     * 계좌 수정
     */
    @PutMapping("/pays/account/{employeeId}")
    public ResponseEntity updateAccount(@PathVariable String employeeId, @RequestBody PayAccountUpdateDto dto) {
        payService.updateAccount(employeeId, dto);
        return new ResponseEntity(redirect(), HttpStatus.MOVED_PERMANENTLY);
    }

    /**
     * 자격 수당 등록
     */
    @PutMapping("/pays/{qualId}")
    public ResponseEntity updateQualPay(@PathVariable Long qualId, @RequestBody QualificationPayRequestDto dto) {
        payService.saveQualPay(qualId, dto);

        return new ResponseEntity(redirect(), HttpStatus.MOVED_PERMANENTLY);
    }

    private HttpHeaders redirect() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/pays/{employeeId}"));
        return headers;
    }

}
