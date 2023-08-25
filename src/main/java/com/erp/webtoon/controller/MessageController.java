package com.erp.webtoon.controller;

import com.erp.webtoon.dto.message.MessageListDto;
import com.erp.webtoon.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    /*
        개인 메시지 조회
     */
    @GetMapping("/{employeeId}")
    public List<MessageListDto> getMessageList(@PathVariable String employeeId) {
        return messageService.getMessageList(employeeId);
    }

    /*
        메시지 상태 변경
     */
    @PatchMapping("/{messageId}/{stat}")
    public void updateMessageStatus(@PathVariable Long messageId, @PathVariable char stat) {
        messageService.modifyStat(messageId, stat);
    }

}
