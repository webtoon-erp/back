package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Document;
import com.erp.webtoon.domain.DocumentRcv;
import com.erp.webtoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRcvRepository extends JpaRepository<DocumentRcv, Long> {

    List<DocumentRcv> findAllByUserAndReceiveTypeAndDocument_StatNot(User user, String rcvType, char stat);

    Optional<DocumentRcv> findByDocumentAndReceiveTypeAndSortSequence(Document document, String receiveType, int sortSequence);

}
