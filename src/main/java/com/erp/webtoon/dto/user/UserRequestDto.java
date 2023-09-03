package com.erp.webtoon.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@Data
public class UserRequestDto {

    @NotBlank
    private String employeeId; // 사번

    @NotBlank
    private String password; // 비밀번호

    @NotBlank
    private String name;    // 이름

    @NotBlank
    private String email;   // 이메일

    @NotBlank
    private String tel; // 전화번호

    @NotNull
    private LocalDate birthDate;   // 생년월일

    @NotBlank
    private String deptName;    // 부서명

    @NotBlank
    private String deptCode; // 부서 코드

    @NotNull
    private int teamNum;    // 팀 번호

    @NotBlank
    private String position;    // 직급

    @NotNull
    private LocalDate joinDate;    // 입사날짜

    private int dayOff;     // 연차개수
}
