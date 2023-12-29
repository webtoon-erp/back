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

    private String employeeId; // 사번

    private String password;    // 비밀번호

    private String name;    // 이름

    private String deptCode;    //부서코드

    private String deptName;    //부서명

    private int teamNum;    // 팀 번호

    private String position;    // 직급

    @Email
    private String email;   // 이메일

    private String tel; // 전화번호

    private LocalDate birthDate;   // 생년월일

    private int dayOff;     // 연차개수
}
