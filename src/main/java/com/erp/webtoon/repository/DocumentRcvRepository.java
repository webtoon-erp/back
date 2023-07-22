package com.erp.webtoon.repository;

import com.erp.webtoon.domain.DocumentRcv;
import com.erp.webtoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRcvRepository extends JpaRepository<DocumentRcv, Long> {

    List<DocumentRcv> findAllByUserAndReceiveTypeAndStat(User user, String rcvType, boolean stat);

}
