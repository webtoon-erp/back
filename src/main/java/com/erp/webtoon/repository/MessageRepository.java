package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
