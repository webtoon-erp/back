package com.erp.webtoon.service;

import com.erp.webtoon.TokenProvider;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.token.TokenResponseDto;
import com.erp.webtoon.dto.user.UserResponseDto;
import com.erp.webtoon.dto.user.UserUpdateDto;
import com.erp.webtoon.repository.RefreshTokenRepository;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

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
    public TokenResponseDto login(String email, String password){
        // 1. Login ID/PW를 기반으로 Authentication 객체 생성
        // 이때 authenticaiton는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 메서드가 실행될 때 CustomUserDetailsService에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponseDto tokenDto = tokenProvider.createToken(authentication, email);

        return tokenDto;
    }
}
