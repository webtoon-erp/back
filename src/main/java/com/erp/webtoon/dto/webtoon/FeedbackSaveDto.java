package com.erp.webtoon.dto.webtoon;

import com.erp.webtoon.domain.Message;
import com.erp.webtoon.domain.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FeedbackSaveDto {

    @NotBlank
    private String msgType;     // 메세지 타입

    @NotBlank
    private String content;     // 메세지 내용

    @NotBlank
    private Long refId; // 참조 ID

    @NotBlank
    private String programId;    // 참조 프로그램ID

    @NotBlank
    private String sendEmpId;
    public Message toEntity(User sendUser) {
        return Message.builder()
                .msgType(msgType)
                .content(content)
                .refId(refId)
                .programId(programId)
                .sendUser(sendUser)
                .build();
    }
}