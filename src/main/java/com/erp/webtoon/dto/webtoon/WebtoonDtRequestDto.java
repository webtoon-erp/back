package com.erp.webtoon.dto.webtoon;

import com.erp.webtoon.domain.Webtoon;
import com.erp.webtoon.domain.WebtoonDt;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@Data
public class WebtoonDtRequestDto {

    private Long webtoonId; // 해당하는 웹툰의 아이디 (조회 용)

    private String subTitle;

    private String content; // 작가의 말

    private String employeeId; // 담당자

    private MultipartFile uploadFile;   // 업로드 파일;

    public WebtoonDt toEntity() {
        return WebtoonDt.builder()
                .subTitle(subTitle)
                .content(content)
                .build();
    }
}
