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

    private URL fileUrl;

    private String category;

    public WebtoonCardViewDto(Webtoon webtoon, String fileName) {
        try {
            this.title = webtoon.getTitle();
            this.webtoonId = webtoon.getId();
            UrlResource resource = new UrlResource("file:" + fileName);
            this.fileUrl = resource.getURL();
            this.category = webtoon.getCategory();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
