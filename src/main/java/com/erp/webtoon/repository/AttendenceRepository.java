package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Attendence;
import com.erp.webtoon.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendenceRepository extends JpaRepository<Attendence, Long> {
}
