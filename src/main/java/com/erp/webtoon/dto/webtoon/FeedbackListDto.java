package com.erp.webtoon.dto.webtoon;

import com.erp.webtoon.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackListDto {

    private String content;     // 메세지 내용

    private String sendUserName; // 발신자 이름

    private String sendUserEmployeeId; // 발신자 사번

}
