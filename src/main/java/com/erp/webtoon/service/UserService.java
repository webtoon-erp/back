package com.erp.webtoon.service;

import com.erp.webtoon.TokenProvider;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.token.LogOutRequestDto;
import com.erp.webtoon.dto.token.TokenRequestDto;
import com.erp.webtoon.dto.token.TokenResponseDto;
import com.erp.webtoon.dto.user.LoginRequestDto;
import com.erp.webtoon.dto.user.UserResponseDto;
import com.erp.webtoon.dto.user.UserUpdateDto;
import com.erp.webtoon.repository.RefreshTokenRepository;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityNotFoundException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입 -> 없나?
     */

    /**
     * 회원 조회
     */
    public UserResponseDto find(String employeeId) {
        User findUser = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

        return UserResponseDto.builder()
                .employeeId(findUser.getEmployeeId())
                .LoginId(findUser.getLoginId())
                .name(findUser.getName())
                .email(findUser.getEmail())
                .tel(findUser.getTel())
                .birthDate(findUser.getBirthDate())
                .deptName(findUser.getDeptName())
                .teamNum(findUser.getTeamNum())
                .position(findUser.getPosition())
                .joinDate(findUser.getJoinDate())
                .dayOff(findUser.getDayOff())
                .build();
    }


    /**
     * 회원 수정
     */
    @Transactional
    public void update(UserUpdateDto dto) {
        User updateUser = userRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

        updateUser.updateInfo(dto.getLoginId(), dto.getPassword(), dto.getName(), dto.getDeptCode(), dto.getDeptName(), dto.getTeamNum(), dto.getPosition(),
                dto.getEmail(), dto.getTel(), dto.getBirthDate(), dto.getDayOff());
    }

    /**
     * 회원 삭제
     */

    /**
     * 로그인
     */
    @Transactional
    public TokenResponseDto login(LoginRequestDto loginRequestDto){
        User user = userRepository.findByLoginId(loginRequestDto.getEmail());
        checkPassword(loginRequestDto.getPassword(), user.getPassword());

        // 1. Login ID/PW를 기반으로 Authentication 객체 생성
        // 이때 authenticaiton는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), user.getPassword());

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 메서드가 실행될 때 CustomUserDetailsService에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponseDto tokenDto = tokenProvider.createToken(authentication, loginRequestDto.getEmail());

        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenDto.getRefreshToken(),
                        tokenDto.getRefreshExpire(), TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    public ResponseEntity<?> reissue(TokenRequestDto reissue) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(reissue.getRefreshToken())) {
            return new ResponseEntity<>("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = tokenProvider.getAuthentication(reissue.getAccessToken());

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            return new ResponseEntity<>("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        if(!refreshToken.equals(reissue.getRefreshToken())) {
            return new ResponseEntity<>("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 4. 새로운 토큰 생성
        TokenResponseDto tokenInfo = tokenProvider.createToken(authentication, reissue.getEmail());

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshExpire(), TimeUnit.MILLISECONDS);

        return new ResponseEntity<>(tokenInfo, HttpStatus.OK);
    }

    public ResponseEntity<?> logout(LogOutRequestDto logout) {
        // 1. Access Token 검증
        if (!tokenProvider.validateToken(logout.getAccessToken())) {
            return new ResponseEntity<>("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = tokenProvider.getAuthentication(logout.getAccessToken());

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = tokenProvider.getExpiration(logout.getAccessToken());
        redisTemplate.opsForValue()
                .set(logout.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return new ResponseEntity<>("로그아웃 되었습니다.", HttpStatus.OK);
    }

    private void checkPassword(String loginPassword, String savedPassword) {
        if(!passwordEncoder.matches(loginPassword, savedPassword)){
            throw new RuntimeException("Password is not matched");
        }
    }
}
