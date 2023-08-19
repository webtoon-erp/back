package com.erp.webtoon.dto.plas;

import com.erp.webtoon.domain.Document;
import com.erp.webtoon.domain.DocumentRcv;
import com.erp.webtoon.domain.User;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class DocumentRcvRequestDto {

    @NotNull
    private int sortSequence; // 순서

    @NotBlank
    private String receiveType;

    @NotBlank
    private String rcvEmployeeId;

    public DocumentRcv toEntity(Document document, User user) {
        return DocumentRcv.builder()
                .sortSequence(sortSequence)
                .receiveType(receiveType)
                .stat('N')
                .document(document)
                .user(user)
                .build();

    }
}
