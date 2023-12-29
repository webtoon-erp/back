package com.erp.webtoon.dto.webtoon;

import com.erp.webtoon.domain.Webtoon;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.net.URL;

@Data
@NoArgsConstructor
public class WebtoonCardViewDto {

    private String title;

    private Long webtoonId;

    private String fileName;

    private String category;

    public WebtoonCardViewDto(Webtoon webtoon, String fileName) {
        this.title = webtoon.getTitle();
        this.webtoonId = webtoon.getId();
        this.fileName = fileName;
        this.category = webtoon.getCategory();
    }
}
