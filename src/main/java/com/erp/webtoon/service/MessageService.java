package com.erp.webtoon.service;

import com.erp.webtoon.domain.Message;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.message.*;
import com.erp.webtoon.repository.MessageRepository;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SlackService slackService;

    /*
        메시지 조회
        - msgType == all
        - msgType == deptCode
        - rcvUser == emp_id
    */
    @Transactional(readOnly = true)
    public List<MessageListDto> getMessageList(String employeeId) {
        User user = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("메시지 수신 직원의 정보가 존재하지 않습니다."));

        List<Message> messageList1 = messageRepository.findByMsgTypeAndStat("all", 'Y');
        List<Message> messageList2 = messageRepository.findByMsgTypeAndStat(user.getDeptCode(), 'Y');
        List<Message> messageList3 = messageRepository.findByRcvUserAndStat(user, 'Y');
        List<Message> messageList = new ArrayList<>();
        messageList.addAll(messageList1);
        messageList.addAll(messageList2);
        messageList.addAll(messageList3);

        return messageList.stream()
                .sorted(Comparator.comparing(Message::getCreatedDate).reversed())
                .limit(5)
                .map(message -> MessageListDto.builder()
                        .content(message.getContent())
                        .refId(message.getRefId())
                        .programId(message.getProgramId())
                        .sendEmployeeId(message.getSendUser().getEmployeeId())
                        .sendName(message.getSendUser().getUsername())
                        .build())
                .collect(Collectors.toList());
    }

    /*
        메시지 상태 변경
        - 수신 -> Y
        - 읽음 -> R
        - 삭제 -> N
    */
    public void modifyStat(Long messageId, char stat) {
        Message message = messageRepository.findById(messageId)
                        .orElseThrow(() -> new EntityNotFoundException("해당 메시지가 존재하지 않습니다."));

        message.changeStat(stat);
    }

    /**
     * 메세지 저장
     */
    public void save(Message message) {
        messageRepository.save(message);
    }

    /*
        메시지 등록
    */
    public void addMsg(Message message) {
        messageRepository.save(message);

        if (message.getMsgType().equals("DM")) {
            // dm일 경우 수신자의 사번을 전달
            slackService.sendSlackChannel(message.getContent(), message.getRcvUser().getEmployeeId());
        } else {
            slackService.sendSlackChannel(message.getContent(), message.getMsgType());
        }
    }

    /*
         참조 ID로 메시지 찾기
     */
    @Transactional(readOnly = true)
    public List<Message> getMessageListByRefId(Long refID) {
        return messageRepository.findByRefId(refID);
    }
}
