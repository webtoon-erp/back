package com.erp.webtoon.repository;

import com.erp.webtoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 사번으로 조회
    Optional<User> findByEmployeeId(String employeeId);
    Optional<User> findByEmail(String email);
    List<User> findAllByDeptCode(String deptCode);
    List<User> findByDeptCodeAndTeamNum(String deptCode, int teamNum);

    // 전체 직원 수 조회
    Long countAllBy();
}
