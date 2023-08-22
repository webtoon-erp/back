package com.erp.webtoon.dto.message;

import com.erp.webtoon.domain.Message;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MessageUpdateDto {

    @NotBlank
    private Long id;

    @NotBlank
    private char stat; // 읽음 -> R, 삭제 -> N

    public Message toEntity() {
        return Message.builder()
                .id(id)
                .stat(stat)
                .build();
    }

}
