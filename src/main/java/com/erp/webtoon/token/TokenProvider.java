package com.erp.webtoon.token;

import com.erp.webtoon.domain.RefreshToken;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.token.TokenResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Collection;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider {

    private final Key key;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final LogoutAccessTokenService logoutAccessTokenService;

    @Autowired
    public TokenProvider(@Value("${spring.jwt.secret}") String secretKey,
                            RefreshTokenService refreshTokenService,
                            LogoutAccessTokenService logoutAccessTokenService,
                            UserDetailsService userDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshTokenService = refreshTokenService;
        this.logoutAccessTokenService = logoutAccessTokenService;
        this.userDetailsService = userDetailsService;
    }

    public TokenResponseDto generateToken(User user) {
        // 1. 토큰 생성
        String accessToken = createAccessToken(user.getEmployeeId(), user.getAuthorities());
        RefreshToken refreshToken = refreshTokenService.save(user.getEmployeeId(), createRefreshToken());

        // 2. 쿠키에 Refresh 토큰 등록
        setRefreshTokenAtCookie(refreshToken);

        // 3. 생성한 토큰을 DTO에 담아 반환
        return TokenResponseDto.from(accessToken, user);
    }

    /**
     * JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메소드
     * @return UserDetails 객체를 통해 만든 Authentication
     */
    public Authentication getAuthentication(String accessToken) {
        String username = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getSubject();
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            throw new IllegalArgumentException("토큰값이 유효하지 않습니다.");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String createAccessToken(String employeeId, Collection<? extends GrantedAuthority> authorities) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(TokenConst.ALG_KEY, SignatureAlgorithm.HS256.getValue())
                .setHeaderParam(TokenConst.TYPE_KEY, TokenConst.TYPE_VALUE)
                .setSubject(employeeId)
                .setIssuedAt(now)   // 토큰 발행 시간
                .setExpiration(new Date(now.getTime() + TokenConst.ACCESS_TOKEN_EXPIRE_TIME))  // 만료시간 : 현재 + 1시간
                .claim(TokenConst.AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken() {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(TokenConst.ALG_KEY, SignatureAlgorithm.HS256.getValue())
                .setHeaderParam(TokenConst.TYPE_KEY, TokenConst.TYPE_VALUE)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TokenConst.REFRESH_TOKEN_EXPIRE_TIME))    // 만료 시간 : 현재 + 6시간
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String resolveToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(TokenConst.TOKEN_PREFIX + " ")) {
            return token.substring(TokenConst.TOKEN_PREFIX.length() + 1);
        }
        return null;
    }

    /**
     * 토큰 정보를 검증하는 메소드
     * @param token 토큰
     * @return 토큰 유효성
     */
    public boolean validateToken(String token) {
        if (checkLogout(token)) {
            log.info("이미 로그아웃한 사용자입니다.");
            return false;
        }

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("유효하지 않은 토큰입니다.");
        } catch (ExpiredJwtException e) {
            log.info("유효 시간이 지났습니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("토큰을 찾을 수 없습니다.");
        }

        return false;
    }

    /**
     * 토큰 파싱 메소드
     * @param accessToken 토큰
     * @return Claims 객체
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * 토큰의 payload 에서 subject(member pk)를 가져오는 메소드
     * @param accessToken 엑세스 토큰 값
     * @return 토큰에 저장되어 있는 회원 pk
     */
    public String parseToken(String accessToken) {
        return parseClaims(accessToken).getSubject();
    }

    public void setRefreshTokenAtCookie(RefreshToken refreshToken) {
        Cookie cookie = new Cookie(TokenConst.TOKEN_TYPE_REFRESH, refreshToken.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(Math.toIntExact(refreshToken.getExpiration()));

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.addCookie(cookie);
    }

    public Long getRemainTime(String token) {
        Date expiration = parseClaims(token).getExpiration();
        Date now = new Date();
        return expiration.getTime() - now.getTime();
    }

    private boolean checkLogout(String token) {
        return logoutAccessTokenService.existsLogoutAccessTokenById(token);
    }
}