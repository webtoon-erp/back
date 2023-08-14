package com.erp.webtoon.fixture;

import com.erp.webtoon.domain.RequestDt;
import com.erp.webtoon.domain.RequestDt.RequestDtBuilder;

public enum RequestDtFixture {

    FIRST_REQUESTDT("컴퓨터", 3, 10000000);

    private final String content;
    private final int count;
    private final int cost;

    RequestDtFixture(String content, int count, int cost) {
        this.content = content;
        this.count = count;
        this.cost = cost;
    }

    public RequestDt 생성(Long id) {
        return 기본_정보_생성()
                .id(id)
                .build();
    }

    private RequestDtBuilder 기본_정보_생성() {
        return RequestDt.builder()
                .content(this.content)
                .count(this.count)
                .cost(this.cost);
    }
}
