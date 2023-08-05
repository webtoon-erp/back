package com.erp.webtoon.service;

import com.erp.webtoon.domain.Message;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.domain.Webtoon;
import com.erp.webtoon.dto.message.*;
import com.erp.webtoon.repository.MessageRepository;
import com.erp.webtoon.repository.UserRepository;
import com.erp.webtoon.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final WebtoonRepository webtoonRepository;
    private final SlackService slackService;

    /*
        메시지 조회
        - msgType == all
        - msgType == deptCode
        - rcvUser == emp_id
    */
    @Transactional(readOnly = true)
    public List<MessageListDto> findMessageList(MessageFindDto dto) {
            User user = userRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("메시지 수신 직원의 정보가 존재하지 않습니다."));

            List<Message> messageList1 = messageRepository.findByMsgType("all");
            List<Message> messageList2 = messageRepository.findByMsgType(user.getDeptCode());
            List<Message> messageList3 = messageRepository.findByRcvUser(user);
            List<Message> messageList = new ArrayList<>();
            messageList.addAll(messageList1);
            messageList.addAll(messageList2);
            messageList.addAll(messageList3);

        return messageList.stream()
                .map(message -> MessageListDto.builder()
                        .content(message.getContent())
                        .refId(message.getRefId())
                        .programId(message.getProgramId())
                        .sendUser(message.getSendUser())
                        .build())
                .collect(Collectors.toList());
    }

    /*
        메시지 상태 변경
        - 수신 -> Y
        - 읽음 -> R
        - 삭제 -> N
    */
    @Transactional
    public void modifyStat(MessageUpdateDto dto) {
        Message message = dto.toEntity();
        char stat = message.getStat();
        message.changeStat(stat);
    }

    /**
     * 메세지 저장
     */
    @Transactional
    public void save(Message message) {
        messageRepository.save(message);
    }

    /*
        메시지 등록
    */
    @Transactional
    public void addMsg(Message message) {
//    public void addMsg(MessageSaveDto dto) throws IOException {

//        User rcvUser = userRepository.findByEmployeeId(dto.getRcvEmpId())
//                .orElseThrow(() -> new EntityNotFoundException("메시지 수신 직원의 정보가 존재하지 않습니다."));
//        User sendUser = userRepository.findByEmployeeId(dto.getSendEmpId())
//                .orElseThrow(() -> new EntityNotFoundException("메시지 발신 직원의 정보가 존재하지 않습니다."));
//
//        Message message = dto.toEntity(rcvUser, sendUser);
        messageRepository.save(message);

        if (message.getMsgType().equals("dm")) {
            // dm일 경우 수신자의 사번을 전달
            slackService.sendSlackChannel(message.getContent(), message.getRcvUser().getEmployeeId());
        }
        else {
            slackService.sendSlackChannel(message.getContent(), message.getMsgType());
        }
    }

    /*
        피드백 조회
        - msgType : webtoon
        - 수신자 : null
    */
    @Transactional(readOnly = true)
    public List<FeedbackListDto> findFeedbackList(Long webtoonId) {
        List<Message> feedbackList = messageRepository.findByRefId(webtoonId);

        return feedbackList.stream()
                .map(feedback -> FeedbackListDto.builder()
                        .content(feedback.getContent())
                        .sendUserName(feedback.getSendUser().getName())
                        .build())
                .collect(Collectors.toList());
    }

    /*
        피드백 등록
        - msgType : webtoon
        - 수신자 : null
    */
    @Transactional
    public void addFeedbackMsg(MessageSaveDto dto) throws IOException {

        User sendUser = userRepository.findByEmployeeId(dto.getSendEmpId())
                .orElseThrow(() -> new EntityNotFoundException("메시지 발신 직원의 정보가 존재하지 않습니다."));

        //피드백 저장
        Message feedbackMsg = dto.toEntity(null, sendUser);
        messageRepository.save(feedbackMsg);

        //메시지 저장
        Webtoon webtoon = webtoonRepository.findById(feedbackMsg.getRefId())
                .orElseThrow(() -> new EntityNotFoundException("웹툰 정보가 존재하지 않습니다."));

        String originContent = feedbackMsg.getContent();
        dto.setContent(webtoon.getTitle() + "에 피드백이 등록되었습니다. \n\n" + originContent);
        addMsg(feedbackMsg);
    }

}
