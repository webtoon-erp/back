package com.erp.webtoon.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Builder
@RedisHash("logout_access_token")
@NoArgsConstructor
@AllArgsConstructor
public class LogoutAccessToken {
    @Id
    private String token;

    @TimeToLive
    private Long expiration;

    public static LogoutAccessToken from(String token, Long expirationTime) {
        return LogoutAccessToken.builder()
                .token(token)
                .expiration(expirationTime / 1000)
                .build();
    }
}

