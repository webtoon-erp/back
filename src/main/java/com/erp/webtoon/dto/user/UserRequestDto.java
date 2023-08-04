package com.erp.webtoon.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String employeeId; // 사번

    private String password; // 비밀번호

    private String name;    // 이름

    private String email;   // 이메일

    private String tel; // 전화번호

    private LocalDate birthDate;   // 생년월일

    private String deptName;    // 부서명

    private String deptCode; // 부서 코드

    private int teamNum;    // 팀 번호

    private String position;    // 직급

    private LocalDate joinDate;    // 입사날짜

    private int dayOff;     // 연차개수
}
