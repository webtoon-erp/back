package com.erp.webtoon.controller;

import com.erp.webtoon.dto.itsm.RequestResponseDto;
import com.erp.webtoon.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KRequestController {

    private final RequestService requestService;

    /**
     * 요청 상세 조회
     */
    @GetMapping("/request/{requestId}")
    public ResponseEntity showOne(@PathVariable Long requestId) {
        RequestResponseDto request = requestService.search(requestId);

        return ResponseEntity.ok(request);
    }

}
