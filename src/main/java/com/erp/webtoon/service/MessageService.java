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

    /*
        메시지 등록
    */
    public void addMsg(MessageRequestDto dto) throws IOException {

        Message message = dto.toEntity();
        messageRepository.save(message);

    }


}
