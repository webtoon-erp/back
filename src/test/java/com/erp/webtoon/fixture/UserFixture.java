package com.erp.webtoon.fixture;

import com.erp.webtoon.domain.User;
import com.erp.webtoon.domain.User.UserBuilder;

import java.time.LocalDate;

public enum UserFixture {

    FIRST_USER("2020120154", "choi123", "최보현","qhgus564@naver.com", "01023456789",
            LocalDate.of(2001,10,16), "IT","IT",1,"신입",
            LocalDate.of(2013,8,1), 10);

    private final String employeeId;
    private final String password;
    private final String name;
    private final String email;
    private String tel;
    private LocalDate birthDate;
    private String deptName;
    private String deptCode;
    private int teamNum;
    private String position;
    private LocalDate joinDate;
    private int dayoff;

    UserFixture(String employeeId, String password, String name, String email, String tel, LocalDate birthDate, String deptName, String deptCode, int teamNum, String position, LocalDate joinDate, int dayoff) {
        this.employeeId = employeeId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.birthDate = birthDate;
        this.deptName = deptName;
        this.deptCode = deptCode;
        this.teamNum = teamNum;
        this.position = position;
        this.joinDate = joinDate;
        this.dayoff = dayoff;
    }

    public User 생성(Long id) {
        return 기본_정보_생성()
                .id(id)
                .build();
    }

    private UserBuilder 기본_정보_생성() {
        return User.builder()
                .employeeId(this.employeeId)
                .password(this.password)
                .name(this.name)
                .email(this.email)
                .tel(this.tel)
                .birthDate(this.birthDate)
                .deptName(this.deptName)
                .deptCode(this.deptCode)
                .teamNum(this.teamNum)
                .position(this.position)
                .joinDate(this.joinDate)
                .dayOff(this.dayoff);
    }
}
