package com.erp.webtoon.dto.webtoon;

import com.erp.webtoon.domain.Webtoon;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
public class WebtoonRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String intro; // 웹툰 소개

    @NotBlank
    private String artist;  //작가 이름

    @NotBlank
    private String illustrator; //그림 작가 이름

    @NotBlank
    private String category;

    @NotBlank
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
