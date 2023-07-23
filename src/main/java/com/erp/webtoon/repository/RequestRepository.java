package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
