package com.erp.webtoon.controller;

import com.erp.webtoon.dto.itsm.CommentListDto;
import com.erp.webtoon.dto.itsm.CommentResponseDto;
import com.erp.webtoon.dto.itsm.RequestDto;
import com.erp.webtoon.dto.itsm.RequestRegisterResponseDto;
import com.erp.webtoon.dto.webtoon.FeedbackListDto;
import com.erp.webtoon.dto.message.MessageSaveDto;
import com.erp.webtoon.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/purchase-request")
    public RequestRegisterResponseDto purchaseRequest(@RequestBody RequestDto requestDto) throws Exception {
        return requestService.purchaseRequest(requestDto);
    }

    @PostMapping("/request")
    public RequestRegisterResponseDto Request(@RequestBody RequestDto requestDto) throws Exception {
        return requestService.assistRequest(requestDto);
    }

    @PostMapping ("/comment")
    public CommentResponseDto registerComment(@RequestBody MessageSaveDto messageSaveDto) throws IOException {
        return requestService.registerComment(messageSaveDto);
    }

    @GetMapping("/comment")
    public List<CommentListDto> getAllComments(@RequestParam Long requestId){
        return requestService.getAllComments(requestId);
    }

    @DeleteMapping("/comment")
    public void deleteComment(@RequestParam Long messageId){
        requestService.deleteComment(messageId);
    }
}
