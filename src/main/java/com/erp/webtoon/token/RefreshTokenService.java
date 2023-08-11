package com.erp.webtoon.token;

import com.erp.webtoon.domain.RefreshToken;
import com.erp.webtoon.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken save(Long memberId, String refreshToken) {
        return refreshTokenRepository.save(
                RefreshToken.from(
                        memberId,
                        refreshToken,
                        TokenConst.REFRESH_TOKEN_EXPIRE_TIME
                )
        );
    }

    public RefreshToken findByEmployeeId(Long memberId) {
        return refreshTokenRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("토큰이 없습니다."));
    }

    public void deleteByEmployeeId(Long memberId) {
        refreshTokenRepository.deleteById(memberId);
    }
}

