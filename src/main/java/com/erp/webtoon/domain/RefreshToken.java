package com.erp.webtoon.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
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
