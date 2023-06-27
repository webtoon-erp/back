package com.erp.webtoon.service;

import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.user.UserResponseDto;
import com.erp.webtoon.dto.user.UserUpdateDto;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

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
    public void update(UserUpdateDto dto) {
        User updateUser = userRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

        updateUser.updateInfo(dto.getLoginId(), dto.getPassword(), dto.getName(), dto.getDeptCode(), dto.getDeptName(), dto.getTeamNum(), dto.getPosition(),
                dto.getEmail(), dto.getTel(), dto.getBirthDate(), dto.getDayOff());
    }

    /**
     * 회원 삭제
     */
}
