package com.erp.webtoon.dto.webtoon;

import com.erp.webtoon.domain.Webtoon;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class WebtoonRequestDto {

    private String title;

    private String intro; // 웹툰 소개

    private String artist;  //작가 이름

    private String illustrator; //그림 작가 이름

    private String category;

    private String keyword;

    private MultipartFile thumbnailFile;

    public Webtoon toEntity() {
        return Webtoon.builder()
                .title(title)
                .intro(intro)
                .artist(artist)
                .illustrator(illustrator)
                .category(category)
                .keyword(keyword).build();
    }
}
