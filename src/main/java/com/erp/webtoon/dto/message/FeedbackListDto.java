package com.erp.webtoon.dto.message;

import com.erp.webtoon.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackListDto {

    private String content;     // 메세지 내용

    private User sendUser;      // 발신자

}
