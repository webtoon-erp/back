package com.erp.webtoon.dto.user;

import com.erp.webtoon.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
public class UserListResponseDto {
    private String position;
    private String name;

    public UserListResponseDto(User user) {
        this.position = user.getPosition();
        this.name = user.getName();
    }
}
