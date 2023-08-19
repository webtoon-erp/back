package com.erp.webtoon.repository;

import com.erp.webtoon.domain.DocumentData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentDataRepository extends JpaRepository<DocumentData, Long> {
}
