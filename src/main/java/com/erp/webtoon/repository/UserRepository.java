package com.erp.webtoon.repository;

import com.erp.webtoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 사번으로 조회
    Optional<User> findByEmployeeId(String employeeId);
}
