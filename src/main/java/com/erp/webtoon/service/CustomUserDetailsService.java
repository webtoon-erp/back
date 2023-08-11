package com.erp.webtoon.service;

import com.erp.webtoon.domain.User;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String employeeId) throws UsernameNotFoundException {
        return userRepository.findByEmployeeId(employeeId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new RuntimeException("해당하는 유저를 찾을 수 없습니다."));
    }

    // 해당하는 User의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(User user) {
        return User.builder()
                .employeeId(user.getEmployeeId())
                .password(passwordEncoder.encode(user.getPassword()))
                .name(user.getName())
                .deptCode(user.getDeptCode())
                .deptName(user.getDeptName())
                .teamNum(user.getTeamNum())
                .position(user.getPosition())
                .email(user.getEmail())
                .tel(user.getTel())
                .birthDate(user.getBirthDate())
                .dayOff(user.getDayOff())
                .build();

    }
}
