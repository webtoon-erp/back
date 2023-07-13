package com.erp.webtoon.dto.message;

import com.erp.webtoon.domain.Message;
import com.erp.webtoon.domain.Program;
import com.erp.webtoon.domain.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MessageRequestDto {

    @NotBlank
    private String msgType;     // 메세지 타입

    @NotBlank
    private String content;     // 메세지 내용

    private Long refId; // 참조 ID

    private Program program;    // 참조 프로그램ID

    private Long rcvUserId;

    private Long sendUserId;
    public Message toEntity(User rcvUser, User sendUser) {
        return Message.builder()
                .msgType(msgType)
                .content(content)
                .refId(refId)
                .program(program)
                .rcvUser(rcvUser)
                .sendUser(sendUser)
                .build();
    }
}