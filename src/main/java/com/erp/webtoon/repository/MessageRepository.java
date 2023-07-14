package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Message;
import com.erp.webtoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByMsgType(String msgType);

    List<Message> findByRcvUser(User rcvUser);

    List<Message> findByRefId(Long refId);

}
