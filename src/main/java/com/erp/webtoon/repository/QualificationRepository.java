package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualificationRepository extends JpaRepository<Qualification, Long> {
}