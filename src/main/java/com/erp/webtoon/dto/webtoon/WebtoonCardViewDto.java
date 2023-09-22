package com.erp.webtoon.dto.webtoon;

import com.erp.webtoon.domain.Webtoon;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebtoonCardViewDto {

    private String title;

    private String fileName;

    private String category;

    public WebtoonCardViewDto(Webtoon webtoon) {
        this.title = webtoon.getTitle();
        this.fileName = webtoon.getFiles().get(webtoon.getFiles().size()-1).getOriginName();
        this.category = webtoon.getCategory();
    }
}
