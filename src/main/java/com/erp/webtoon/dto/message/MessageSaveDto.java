package com.erp.webtoon.dto.message;

import com.erp.webtoon.domain.Message;
import com.erp.webtoon.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class MessageSaveDto {

    @NotBlank
    private String channel;     // 메세지 타입

    @NotBlank
    private String content;     // 메세지 내용

    private Long refId; // 참조 ID

    private String programId;    // 참조 프로그램ID

    private String stat;

    private String rcvEmpId;

    private String sendEmpId;

    public Message toEntity(User rcvUser, User sendUser) {
        return Message.builder()
                .msgType(channel)
                .content(content)
                .refId(refId)
                .programId(programId)
                .stat('Y')
                .rcvUser(rcvUser)
                .sendUser(sendUser)
                .build();
    }
}