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

    public RefreshToken save(String employeeId, String refreshToken) {
        return refreshTokenRepository.save(
                RefreshToken.from(
                        Long.parseLong(employeeId),
                        refreshToken,
                        TokenConst.REFRESH_TOKEN_EXPIRE_TIME
                )
        );
    }

    public RefreshToken findByEmployeeId(Long memberId) {
        return refreshTokenRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("토큰이 없습니다."));
    }

    public void deleteByEmployeeId(String memberId) {
        refreshTokenRepository.deleteByEmployeeId(memberId);
    }
}

