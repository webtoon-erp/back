package com.erp.webtoon.repository;

import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.user.UserListResponseDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 사번으로 조회
    Optional<User> findByEmployeeId(String employeeId);
    Optional<User> findByEmail(String email);
    List<User> findAllByDeptCode(String deptCode);
    // 전체 직원 수 조회
    Long countAllBy();

    // 전체 부서 리스트 조회
    List<String> findDistinctDeptCodeBy();
}
