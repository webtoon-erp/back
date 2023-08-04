package com.erp.webtoon.controller;

import com.erp.webtoon.dto.itsm.RequestDto;
import com.erp.webtoon.dto.itsm.RequestResponseDto;
import com.erp.webtoon.dto.message.MessageSaveDto;
import com.erp.webtoon.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/purchase-request")
    public RequestResponseDto purchaseRequest(@RequestBody RequestDto requestDto) throws Exception {
        return requestService.purchaseRequest(requestDto);
    }

    @PostMapping("request")
    public RequestResponseDto Request(@RequestBody RequestDto requestDto) throws Exception {
        return requestService.assistRequest(requestDto);
    }

    @PostMapping void registerComment(@RequestBody MessageSaveDto messageSaveDto) throws IOException {
        requestService.registerComment(messageSaveDto);
    }
}
