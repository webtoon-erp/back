package com.erp.webtoon.controller;

import com.erp.webtoon.dto.message.MessageListDto;
import com.erp.webtoon.dto.message.MessageUpdateDto;
import com.erp.webtoon.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /*
        메시지 조회
     */
    @GetMapping("/message/{employeeId}")
    public List<MessageListDto> message(@PathVariable String employeeId) {
        return messageService.getMessageList(employeeId);
    }

    /*
        메시지 상태 변경
     */
    @GetMapping("/message/updateStat")
    public void messageModify(@RequestBody MessageUpdateDto dto) {
        messageService.modifyStat(dto);
    }

}
