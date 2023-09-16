package com.erp.webtoon.dto.token;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenRequestDto {
    private String email;
    private String accessToken;
    private String refreshToken;
}
