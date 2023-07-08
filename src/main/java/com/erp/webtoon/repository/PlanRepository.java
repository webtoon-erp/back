package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByMonth(int month);
}
