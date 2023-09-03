package com.erp.webtoon.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
public class TokenRequestDto {
    private String email;
    private String accessToken;
    private String refreshToken;
}
