package com.erp.webtoon.dto.plas;

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

}
