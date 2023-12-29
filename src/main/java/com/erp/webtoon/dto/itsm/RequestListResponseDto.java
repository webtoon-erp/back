package com.erp.webtoon.dto.itsm;

import com.erp.webtoon.domain.Request;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestListResponseDto {

    private Long reqId;

    private String reqType;

    private String title;

    private int step; // 1: 요청 2: 접수 3: 진행 4: 완료 5: 반려

    private LocalDate dueDate; // 마감기한

    private LocalDate doneDate;    // 완료 일자

    private String reqUser; // 요청자 사번

    private String itUser;  // 담당자 사번

    public RequestListResponseDto(Request request) {
        this.reqId = request.getId();
        this.reqType = request.getReqType();
        this.title = request.getTitle();
        this.step = request.getStep();
        this.dueDate = request.getDueDate();
        this.doneDate = request.getDoneDate();
        this.reqUser = request.getReqUser().getEmployeeId();
        this.itUser = request.getItUser().getEmployeeId();
    }
}
