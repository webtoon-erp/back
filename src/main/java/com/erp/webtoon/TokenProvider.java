package com.erp.webtoon;

import com.erp.webtoon.domain.RefreshToken;
import com.erp.webtoon.dto.token.TokenResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

//모든 속성이 BeanFactory에 의해 설정되면 반응해야 하는 빈에 의해 구현되는 인터페이스
@Component
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private static final int ACCESS_TOKEN_EXPIRES = 1000 * 60 * 30; // 30분
    private static final int REFRESH_TOKEN_EXPIRES = 1000 * 60 * 60 * 24 * 7; // 7일
    // access 토큰 만료 시간 생성
    Date now = new Date();
    Date accessTokenExpireTime = new Date(now.getTime() + ACCESS_TOKEN_EXPIRES);
    // refresh 토큰 만료 시간 생성
    Date refreshTokenExpireTime = new Date(now.getTime() + REFRESH_TOKEN_EXPIRES);


    private final String secret;
    private Key key;

    public TokenProvider(@Value("${spring.jwt.secret}") String secret){
        this.secret = secret;
    }

    // 빈이 생성되고 주입을 받은 후에 secret 값을 Base64 Decode해서 key 변수에 할당하기 위해
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenResponseDto createToken(Authentication authentication, String userEmail){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities) // 정보 저장
                .setIssuedAt(now)
                .setExpiration(accessTokenExpireTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // refresh 토큰 생성
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpireTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenResponseDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshExpire(REFRESH_TOKEN_EXPIRES)
                .userEmail(userEmail)
                .build();
    }

    // 토큰으로 클레임을 만들고 이를 이용해 유저 객체를 만들어서 최종적으로 authentication 객체 리턴
    public Authentication getAuthentication(String accessToken){
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if(claims.get("auth") == null){
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰의 유효성 검증을 수행
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e){
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e){
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e){
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String validateRefreshToken(RefreshToken refreshTokenObj){
        // refresh 객체에서 refreshToken 추출
        String refreshToken = refreshTokenObj.getRefreshToken();

        try {
            // 검증
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);

            // refresh 토큰의 만료시간이 지나지 않았을 경우, 새로운 access 토큰 생성
            if (!claims.getBody().getExpiration().before(new Date())){
                return recreationAccessToekn(claims.getBody().get("sub").toString(), claims.getBody().get("roles"));
            }
        } catch (Exception e){
            // refresh 토큰이 만료되었을 경우, 로그인이 필요함
            return null;
        }
        return null;
    }

    private String recreationAccessToekn(String email, Object roles) {
        // JWT payload 에 저장되는 정보 단위
        Claims claims = Jwts.claims().setSubject(email);
        // 정보는 key/value 쌍으로 저장됨
        claims.put("roles", roles);
        Date now = new Date();

        // Access Token 재발급
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return accessToken;
    }

    private Claims parseClaims(String accessToken){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
}