package com.erp.webtoon.dto.webtoon;

import com.erp.webtoon.domain.Webtoon;
import lombok.Builder;
import lombok.Data;

@Data
public class WebtoonListResponseDto {

    private Long id;
    private String title;

    private String artist;  //작가 이름

    private String category;

    private String keyword;

    public WebtoonListResponseDto(Webtoon webtoon) {
        this.id = webtoon.getId();
        this.title = webtoon.getTitle();
        this.artist = webtoon.getArtist();
        this.category = webtoon.getCategory();
        this.keyword = webtoon.getKeyword();
    }

    // 썸네일 파일은 상세조회 시에만 보이도록!...
}
