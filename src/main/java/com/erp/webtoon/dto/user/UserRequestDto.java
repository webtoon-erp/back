package com.erp.webtoon.dto.user;

import com.erp.webtoon.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank
    private String employeeId; // 사번

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

    public User toEntity(String encodedPassword) {
        User user = User.builder()
                .employeeId(employeeId)
                .password(encodedPassword)
                .name(name)
                .email(email)
                .tel(tel)
                .birthDate(birthDate)
                .deptName(deptName)
                .deptCode(deptCode)
                .teamNum(teamNum)
                .position(position)
                .joinDate(joinDate)
                .dayOff(0)
                .usable(true)
                .build();
        return user;
    }
}
