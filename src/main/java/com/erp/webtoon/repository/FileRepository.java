package com.erp.webtoon.repository;

import com.erp.webtoon.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
