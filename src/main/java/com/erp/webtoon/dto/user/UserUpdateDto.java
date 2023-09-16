package com.erp.webtoon.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserUpdateDto {

    @NotBlank
    private String employeeId; // 사번

    @NotBlank
    private String password;    // 비밀번호

    @NotBlank
    private String name;    // 이름

    @NotBlank
    private String deptCode;    //부서코드

    @NotBlank
    private String deptName;    //부서명

    @NotNull
    private int teamNum;    // 팀 번호

    @NotBlank
    private String position;    // 직급

    @NotBlank
    @Email
    private String email;   // 이메일

    @NotBlank
    private String tel; // 전화번호

    @NotBlank
    private LocalDate birthDate;   // 생년월일

    @NotNull
    private int dayOff;     // 연차개수
}
