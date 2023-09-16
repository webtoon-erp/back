package com.erp.webtoon.dto.webtoon;

import com.erp.webtoon.domain.WebtoonDt;
import lombok.Data;

@Data
public class WebtoonDtRequestDto {

    private Long webtoonId; // 해당하는 웹툰의 아이디 (조회 용)

    private String subTitle;

    private String content; // 작가의 말

    private String employeeId; // 담당자

    public WebtoonDt toEntity() {
        return WebtoonDt.builder()
                .subTitle(subTitle)
                .content(content)
                .build();
    }
}
