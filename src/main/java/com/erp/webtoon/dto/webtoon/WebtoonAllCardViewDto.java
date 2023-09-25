package com.erp.webtoon.dto.webtoon;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WebtoonAllCardViewDto {

    private List<WebtoonCardViewDto> notFinalWebtoons;

    private List<WebtoonCardViewDto> finalWebtoons;

    @Builder
    public WebtoonAllCardViewDto(List<WebtoonCardViewDto> notFinalWebtoons, List<WebtoonCardViewDto> finalWebtoons) {
        this.notFinalWebtoons = notFinalWebtoons;
        this.finalWebtoons = finalWebtoons;
    }
}
