package com.erp.webtoon.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @Column(name = "member_id", unique = true)
    private Long employeeId;

    @Column(unique = true)
    private String refreshToken;

    private Long expiration;

    public static RefreshToken from(Long employeeId, String token, Long expirationTime) {
        return RefreshToken.builder()
                .employeeId(employeeId)
                .refreshToken(token)
                .expiration(expirationTime)
                .build();
    }
}
