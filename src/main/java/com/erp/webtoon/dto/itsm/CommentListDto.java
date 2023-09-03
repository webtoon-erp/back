package com.erp.webtoon.dto.itsm;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentListDto {

    private String content;     // 메세지 내용

    private String sendUserDeptName; // 발신자 부서명

    private String sendUserEmployeeId; // 발신자 사번

    private String sendUserName; // 발신자 이름
}
