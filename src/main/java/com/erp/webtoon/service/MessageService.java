package com.erp.webtoon.service;

import com.erp.webtoon.domain.Message;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.message.FeedbackListDto;
import com.erp.webtoon.dto.message.MessageListDto;
import com.erp.webtoon.dto.message.MessageRequestDto;
import com.erp.webtoon.dto.message.MessageUpdateDto;
import com.erp.webtoon.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final SlackService slackService;

    /*
        메시지 조회
        - msgType == all
        - msgType == deptCode
        - rcvUser == emp_id
    */
    @Transactional(readOnly = true)
    public List<MessageListDto> findMessageList(User user) {
        List<Message> messageList = messageRepository.findByMsgTypeOrMsgTypeOrRcvUser("all", user.getDeptCode(), user);

        return messageList.stream()
                .map(message -> MessageListDto.builder()
                        .content(message.getContent())
                        .refId(message.getRefId())
                        .program(message.getProgram())
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
    public void modifyStat(MessageUpdateDto dto) {
        Message message = dto.toEntity();
        char stat = message.getStat();
        message.changeStat(stat);
    }

    /*
        메시지 등록
    */
    public void addMsg(MessageRequestDto dto) throws IOException {

        Message message = dto.toEntity();
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
                        .sendUser(feedback.getSendUser())
                        .build())
                .collect(Collectors.toList());
    }


}
