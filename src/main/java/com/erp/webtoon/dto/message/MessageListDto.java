package com.erp.webtoon.dto.message;

import com.erp.webtoon.domain.Program;
import com.erp.webtoon.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageListDto {

    private String content;     // 메세지 내용

    private Long refId;         // 참조 ID

    private Program program;    // 참조 프로그램ID

    private User sendUser;      // 발신자

}
