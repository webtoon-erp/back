package com.erp.webtoon.controller;

import com.erp.webtoon.dto.itsm.RequestDto;
import com.erp.webtoon.dto.itsm.RequestResponseDto;
import com.erp.webtoon.dto.message.FeedbackListDto;
import com.erp.webtoon.dto.message.MessageSaveDto;
import com.erp.webtoon.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/purchase-request")
    public RequestResponseDto purchaseRequest(@RequestBody RequestDto requestDto) throws Exception {
        return requestService.purchaseRequest(requestDto);
    }

    @PostMapping("/request")
    public RequestResponseDto Request(@RequestBody RequestDto requestDto) throws Exception {
        return requestService.assistRequest(requestDto);
    }

    @PostMapping ("/comment")
    public void registerComment(@RequestBody MessageSaveDto messageSaveDto) throws IOException {
        requestService.registerComment(messageSaveDto);
    }

    @GetMapping("/comment")
    public List<FeedbackListDto> getAllComments(@RequestBody Long requestId){
        return requestService.getAllComments(requestId);
    }

    @DeleteMapping("/comment")
    public void deleteComment(@RequestBody Long messageId){
        requestService.deleteComment(messageId);
    }
}
