package com.erp.webtoon.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlackRequestDto {
    private String email;
    private String title;
    private String message;
}
