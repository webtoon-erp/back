package com.erp.webtoon.service;

import com.erp.webtoon.domain.User;
import com.erp.webtoon.domain.Webtoon;
import com.erp.webtoon.dto.user.UserListResponseDto;
import com.erp.webtoon.dto.webtoon.WebtoonListResponseDto;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HrService {

    private UserRepository userRepository;

    /**
     * 현재 직원 리스트 조회
     */
    public List<UserListResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserListResponseDto> userList = new ArrayList<>();

        for (User user : users) {
            userList.add(UserListResponseDto.builder()
                    .position(user.getPosition())
                    .name(user.getName())
                    .build());
        }

        return userList;
    }

    public List<UserListResponseDto> searchPosts(String keyword) {
        List<UserListResponseDto> userList = userRepository.findAllByPositionContaining(keyword);
        return userList;
    }
}
