package com.erp.webtoon.token;

import com.erp.webtoon.domain.LogoutAccessToken;
import com.erp.webtoon.repository.LogoutAccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutAccessTokenService {
    private final LogoutAccessTokenRepository logoutAccessTokenRepository;

    public void saveLogoutAccessToken(LogoutAccessToken logoutAccessToken) {
        logoutAccessTokenRepository.save(logoutAccessToken);
    }

    public boolean existsLogoutAccessTokenById(String accessToken) {
        return logoutAccessTokenRepository.existsById(accessToken);
    }
}
