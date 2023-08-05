package com.erp.webtoon.dto.pay;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PayAccountUpdateDto {

    @NotBlank
    private String bankAccount;
}
