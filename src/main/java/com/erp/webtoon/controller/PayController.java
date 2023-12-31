package com.erp.webtoon.controller;

import com.erp.webtoon.dto.pay.PayAccountUpdateDto;
import com.erp.webtoon.dto.pay.PayAllListResponseDto;
import com.erp.webtoon.dto.pay.PayDateUpdateListDto;
import com.erp.webtoon.dto.pay.PayMonthUpdateDto;
import com.erp.webtoon.dto.pay.PayRequestDto;
import com.erp.webtoon.dto.pay.PayResponseDto;
import com.erp.webtoon.dto.pay.QualificationPayRequestDto;
import com.erp.webtoon.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    /**
     * 월 급여 등록
     */
    @PostMapping("/pays")
    public ResponseEntity save(@RequestBody PayRequestDto dto) {
        payService.save(dto);
        return new ResponseEntity(redirect(dto.getEmployeeId()), HttpStatus.OK);
    }

    /**
     * 개인 급여 조회
     */
    @GetMapping("/pays/{employeeId}")
    public ResponseEntity show(@PathVariable String employeeId) throws MalformedURLException {
        PayResponseDto dto = payService.search(employeeId);

        return ResponseEntity.ok(dto);
    }

    /**
     * 전체 급여 조회
     */
    @GetMapping("/pays/all")
    public ResponseEntity showAll(){
        List<PayAllListResponseDto> dtos = payService.allPayList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * 월 급여 수정
     */
    @PutMapping("/pays/month/{employeeId}")
    public ResponseEntity update(@PathVariable String employeeId, @RequestBody PayMonthUpdateDto dto) {
        payService.updateMonthPay(employeeId, dto);

        return ResponseEntity.ok(redirect(employeeId));
    }

    /**
     * 계좌 수정
     */
    @PutMapping("/pays/account/{employeeId}")
    public ResponseEntity updateAccount(@PathVariable String employeeId, @RequestBody PayAccountUpdateDto dto) {
        payService.updateAccount(employeeId, dto);
        return ResponseEntity.ok(redirect(employeeId));
    }

    /**
     * 지급일 여러명 수정
     */
    @PutMapping("/pays/date")
    public ResponseEntity updatePayDate(@RequestBody List<PayDateUpdateListDto> dtos) {
        payService.updateAllPayDate(dtos);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 자격 수당 등록
     */
    @PutMapping("/pays/qualification")
    public ResponseEntity updateQualPay(@RequestBody List<QualificationPayRequestDto> dtos) {
        payService.saveQualPay(dtos);

        return new ResponseEntity(HttpStatus.OK);
    }

    private Map<String, URI> redirect(String employeeId) {
        HttpHeaders headers = new HttpHeaders();
        URI uri = UriComponentsBuilder.newInstance()
                .path("/pays/{employeeId}")
                .buildAndExpand(employeeId).toUri();

        Map<String, URI> location = new HashMap<>();
        location.put("uri", uri);

        return location;
    }
}
