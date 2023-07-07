package com.erp.webtoon.dto.webtoon;

import com.erp.webtoon.domain.Webtoon;
import com.erp.webtoon.domain.WebtoonDt;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@Data
public class WebtoonDtRequestDto {

    private Long webtoonId; // 해당하는 웹툰의 아이디 (조회 용)

    private String episodeNum;

    private String subTitle;

    private MultipartFile uploadFile;   // 업로드 파일;

    public WebtoonDt toEntity(Webtoon webtoon) {
        return WebtoonDt.builder()
                .episodeNum(episodeNum)
                .subTitle(subTitle)
                .webtoon(webtoon)
                .build();
    }

}
