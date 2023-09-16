package com.erp.webtoon.repository;

import com.erp.webtoon.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    void deleteByEmployeeId(Long employeeId);
}
