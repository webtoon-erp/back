package com.erp.webtoon.fixture;

import com.erp.webtoon.domain.Request;
import com.erp.webtoon.domain.Request.RequestBuilder;

import java.time.LocalDate;

public enum RequestFixture {

    FIRST_REQUEST("구매", "구매요청입니다", "컴퓨터 사죠", 1, LocalDate.of(2023,8,1), LocalDate.of(2023,8,11));

    private final String reqType;
    private final String title;
    private final String content;
    private final int step;
    private final LocalDate dueDate;
    private final LocalDate doneDate;

    RequestFixture(String reqType, String title, String content, int step, LocalDate dueDate, LocalDate doneDate) {
        this.reqType = reqType;
        this.title = title;
        this.content = content;
        this.step = step;
        this.dueDate = dueDate;
        this.doneDate = doneDate;
    }

    public Request 생성(Long id) {
        return 기본_정보_생성()
                .id(id)
                .build();
    }

    private RequestBuilder 기본_정보_생성() {
        return Request.builder()
                .reqType(this.reqType)
                .title(this.title)
                .content(this.content)
                .step(this.step)
                .dueDate(this.dueDate)
                .doneDate(this.doneDate);
    }
}
