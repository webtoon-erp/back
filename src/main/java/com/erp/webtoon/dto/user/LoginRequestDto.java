package com.erp.webtoon.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
@Data
@NoArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "사원 번호는 필수입니다.")
    private String employeeId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
