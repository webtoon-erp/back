package com.erp.webtoon.dto.token;

import com.erp.webtoon.domain.User;
import com.erp.webtoon.token.TokenConst;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponseDto {
    private String grantType;
    private String accessToken;
    private String employeeId;
    private String position;

    public static TokenResponseDto from(String accessToken, User user) {
        return TokenResponseDto.builder()
                .grantType(TokenConst.TOKEN_PREFIX)
                .accessToken(accessToken)
                .employeeId(user.getEmployeeId())
                .position(user.getPosition())
                .build();
    }
}
