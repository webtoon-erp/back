package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Document;
import com.erp.webtoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findDocumentByWriteUser(User user);

}
