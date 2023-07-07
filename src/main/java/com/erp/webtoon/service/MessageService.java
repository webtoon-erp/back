package com.erp.webtoon.service;

import com.erp.webtoon.domain.Message;
import com.erp.webtoon.dto.message.MessageRequestDto;
import com.erp.webtoon.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final SlackService slackService;

    /*
        메시지 등록
    */
    public void addMsg(MessageRequestDto dto) throws IOException {

        Message message = dto.toEntity();
        messageRepository.save(message);

        if (message.getMsgType().equals("dm")) {
            // dm일 경우 수신자의 사번을 전달
            slackService.sendSlackChannel(message.getContent(), message.getRcvUser().getEmployeeId());
        }
        else {
            slackService.sendSlackChannel(message.getContent(), message.getMsgType());
        }
    }


}
