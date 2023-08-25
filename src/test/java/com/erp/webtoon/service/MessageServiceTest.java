package com.erp.webtoon.service;

import com.erp.webtoon.domain.Message;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.repository.MessageRepository;
import com.erp.webtoon.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class MessageServiceTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        messageRepository.deleteAll();
    }

    @Test
    void testAddMsg_DMMessageType() {
        // given
        User rcvUser = User.builder()
                .deptCode("HR")
                .email("gomsonixx@gmail.com")
                .employeeId("TEST1")
                .build();

        userRepository.save(rcvUser);

        Message message = Message.builder()
                .msgType("DM")
                .content("Test DM message")
                .rcvUser(rcvUser)
                .build();

        // when
        messageService.addMsg(message);

        // then
        Message savedMessage = messageRepository.findById(message.getId()).orElse(null);
        assertNotNull(savedMessage); // 메시지가 실제 데이터베이스에 저장되었는지 확인
        assertEquals("DM", savedMessage.getMsgType());
        assertEquals("Test DM message", savedMessage.getContent());
    }


}
