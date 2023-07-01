package com.erp.webtoon.repository;

import com.erp.webtoon.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    boolean existsByEmail(String keyUserEmail);
    void deleteByEmail(String keyUserEmail);
}
