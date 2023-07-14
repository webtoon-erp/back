package com.erp.webtoon.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserListResponseDto {
    private String position;
    private String name;
}
