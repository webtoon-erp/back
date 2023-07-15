package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay, Long> {
}
