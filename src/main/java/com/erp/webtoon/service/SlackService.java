package com.erp.webtoon.service;

import com.erp.webtoon.domain.User;
import com.erp.webtoon.repository.UserRepository;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import com.erp.webtoon.SlackConstant;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.users.UsersLookupByEmailRequest;
import com.slack.api.methods.SlackApiException;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
@Service
@Slf4j
@RequiredArgsConstructor
public class SlackService {

    private final UserRepository userRepository;

    @Value(value = "${slack.token}")
    String slackToken;

    public void sendSlackChannel(String message, String channel){

        String channelAddress = "";

        // 채널 분기
        switch (channel) {
            case "test":
                channelAddress = SlackConstant.TEST_CHANNEL; // 테스트
                break;

            case "all":
                channelAddress = SlackConstant.ALL_CHANNEL;  // 전체 알림
                break;

            case "webtoon":
                channelAddress = SlackConstant.WT_CHANNEL;   // 웹툰
                break;

            case "it":
                channelAddress = SlackConstant.IT_CHANNEL;   // IT팀
                break;

            default:
                channelAddress = getSlackIDByEmail(findUserEmail(channel));  // 개인DM
        }

        try{
            MethodsClient methods = Slack.getInstance().methods(slackToken);

            ChatPostMessageRequest request = ChatPostMessageRequest
                    .builder()
                    .channel(channelAddress)
                    .text(message)
                    .build();

            methods.chatPostMessage(request);

            log.info("Slack " + channel + " 에 메시지 보냄");
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }

    public String getSlackIDByEmail(String email) {

        String slackID = "";

        try{
            MethodsClient methods = Slack.getInstance().methods(slackToken);

            UsersLookupByEmailRequest request = UsersLookupByEmailRequest
                    .builder()
                    .email(email)
                    .build();

            UsersLookupByEmailResponse response =  methods.usersLookupByEmail(request);

            log.info("Slack " + email + " 을 사용하는 멤버ID 찾음");

            slackID = response.getUser().getId();

        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }

        return slackID;
    }
    public String findUserEmail(String employeeId) {
        User user = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 직원의 사번이 존재하지 않습니다."));

        return user.getEmail();
    }
}
