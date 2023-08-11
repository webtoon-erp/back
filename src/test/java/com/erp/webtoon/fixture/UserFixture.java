package com.erp.webtoon.fixture;

import com.erp.webtoon.domain.User;
import com.erp.webtoon.domain.User.UserBuilder;

public enum UserFixture {

    FIRST_USER("2020120154", "최보현","qhgus564@naver.com");

    private final String employeeId;
    private final String name;
    private final String email;

    UserFixture(String employeeId, String name, String email) {
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
    }

    public User 생성(Long id) {
        return 기본_정보_생성()
                .id(id)
                .build();
    }

    private UserBuilder 기본_정보_생성() {
        return User.builder()
                .employeeId(this.employeeId)
                .name(this.name)
                .email(this.email);
    }
}
