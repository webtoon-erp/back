package com.erp.webtoon.dto.itsm;

import com.erp.webtoon.domain.Message;
import com.erp.webtoon.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentSaveDto {

    @Builder.Default
    private String msgType = "FEEDBACK";     // 메세지 타입

    @NotBlank
    private String content;     // 메세지 내용

    @NotBlank
    private Long refId; // 참조 ID

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
