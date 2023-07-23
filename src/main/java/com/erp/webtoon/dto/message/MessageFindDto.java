package com.erp.webtoon.dto.message;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MessageFindDto {

    @NotBlank
    private String employeeId;

}
