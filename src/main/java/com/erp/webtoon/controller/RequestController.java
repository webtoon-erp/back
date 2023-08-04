package com.erp.webtoon.controller;

import com.erp.webtoon.dto.itsm.RequestDto;
import com.erp.webtoon.dto.itsm.RequestResponseDto;
import com.erp.webtoon.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/purchase-request")
    public RequestResponseDto purchaseRequest(@RequestBody RequestDto requestDto) throws Exception {
        return requestService.purchaseRequest(requestDto);
    }
}
