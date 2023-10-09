package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Message;
import com.erp.webtoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByMsgTypeAndStat(String msgType, char stat);

    List<Message> findByRcvUserAndStat(User rcvUser, char stat);

    List<Message> findByRefIdAndMsgType(Long refId, String msgType);
}
