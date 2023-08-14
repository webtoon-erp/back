package com.erp.webtoon.fixture;

import com.erp.webtoon.domain.Qualification;
import com.erp.webtoon.domain.Qualification.QualificationBuilder;

import java.time.LocalDate;

public enum QualificationFixture {

    FIRST_QUALIFICATION("자격증", "정보처리기사", LocalDate.of(2023,6,25), 500000),
    SECOND_QUALIFICATION("자격증", "운전면허증", LocalDate.of(2022,5,5), 300000);

    private final String qlfcType;
    private final String content;
    private final LocalDate qlfcDate;
    private final int qlfcPay;

    QualificationFixture(String qlfcType, String content, LocalDate qlfcDate, int qlfcPay) {
        this.qlfcType = qlfcType;
        this.content = content;
        this.qlfcDate = qlfcDate;
        this.qlfcPay = qlfcPay;
    }

    public Qualification 생성(Long id) {
        return 기본_정보_생성()
                .id(id)
                .build();
    }

    private QualificationBuilder 기본_정보_생성() {
        return Qualification.builder()
                .qlfcType(this.qlfcType)
                .content(this.content)
                .qlfcDate(this.qlfcDate)
                .qlfcPay(this.qlfcPay);
    }
}