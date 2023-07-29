package com.erp.webtoon.dto.webtoon;

import com.erp.webtoon.domain.Webtoon;
import lombok.Builder;
import lombok.Data;

@Data
public class WebtoonListResponseDto {

    private Long id;    //프론트 검색용

    private String title;

    private String artist;  //작가 이름

    private String illustrator; // 그림 작가

    private String category; // 요일

    private String keyword;

    public WebtoonListResponseDto(Webtoon webtoon) {
        this.id = webtoon.getId();
        this.title = webtoon.getTitle();
        this.artist = webtoon.getArtist();
        this.illustrator = webtoon.getIllustrator();
        this.category = webtoon.getCategory();
        this.keyword = webtoon.getKeyword();
    }

    // 썸네일 파일은 상세조회 시에만 보이도록!...
}
