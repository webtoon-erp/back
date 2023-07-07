package com.erp.webtoon.service;

import com.erp.webtoon.domain.Webtoon;
import com.erp.webtoon.dto.webtoon.WebtoonResponseDto;
import com.erp.webtoon.dto.webtoon.WebtoonEpisodeDto;
import com.erp.webtoon.dto.webtoon.WebtoonListResponseDto;
import com.erp.webtoon.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;

    /**
     * 등록 웹툰 생성 필요?
     */

    /**
     * 등록 웹툰 리스트 조회
     */
    public List<WebtoonListResponseDto> getAllWebtoon() {
        List<Webtoon> webtoons = webtoonRepository.findAll();

        List<WebtoonListResponseDto> dtos = new ArrayList<>();

        for (Webtoon webtoon : webtoons) {
            dtos.add(WebtoonListResponseDto.builder()
                    .id(webtoon.getId())
                    .title(webtoon.getTitle())
                    .artist(webtoon.getArtist())
                    .category(webtoon.getCategory())
                    .keyword(webtoon.getKeyword())
                    .build());
        }

        return dtos;
    }

    /**
     * 등록 웹툰 검색 조회 -> 제목 / 작가 / 카테고리 / 키워드 별
     */

    //제목 검색
    public List<WebtoonListResponseDto> getTitleWebtoon(String title) {
        List<Webtoon> webtoons = webtoonRepository.findByTitle(title);

        // 해당하는 웹툰이 없는 경우
        if(webtoons != null || webtoons.isEmpty()) {
            throw new EntityNotFoundException("해당하는 제목의 웹툰이 없습니다.");
        }

        List<WebtoonListResponseDto> dtos = new ArrayList<>();

        for (Webtoon webtoon : webtoons) {
            dtos.add(WebtoonListResponseDto.builder()
                    .id(webtoon.getId())
                    .title(webtoon.getTitle())
                    .artist(webtoon.getArtist())
                    .category(webtoon.getCategory())
                    .keyword(webtoon.getKeyword())
                    .build());
        }
        return dtos;
    }

    //작가 검색
    public List<WebtoonListResponseDto> getArtistWebtoon(String artist) {
        List<Webtoon> webtoons = webtoonRepository.findByArtist(artist);

        // 해당하는 웹툰이 없는 경우
        if(webtoons != null || webtoons.isEmpty()) {
            throw new EntityNotFoundException("해당하는 작가의 웹툰이 없습니다.");
        }

        List<WebtoonListResponseDto> dtos = new ArrayList<>();

        for (Webtoon webtoon : webtoons) {
            dtos.add(WebtoonListResponseDto.builder()
                    .id(webtoon.getId())
                    .title(webtoon.getTitle())
                    .artist(webtoon.getArtist())
                    .category(webtoon.getCategory())
                    .keyword(webtoon.getKeyword())
                    .build());
        }
        return dtos;
    }

    //카테고리 검색
    public List<WebtoonListResponseDto> getCategoryWebtoon(String category) {
        List<Webtoon> webtoons = webtoonRepository.findByCategory(category);

        // 해당하는 웹툰이 없는 경우
        if(webtoons != null || webtoons.isEmpty()) {
            throw new EntityNotFoundException("해당하는 카테고리의 웹툰이 없습니다.");
        }

        List<WebtoonListResponseDto> dtos = new ArrayList<>();

        for (Webtoon webtoon : webtoons) {
            dtos.add(WebtoonListResponseDto.builder()
                    .id(webtoon.getId())
                    .title(webtoon.getTitle())
                    .artist(webtoon.getArtist())
                    .category(webtoon.getCategory())
                    .keyword(webtoon.getKeyword())
                    .build());
        }
        return dtos;
    }

    // 키워드 검색
    public List<WebtoonListResponseDto> getKeywordWebtoon(String keyword) {
        List<Webtoon> webtoons = webtoonRepository.findByKeyword(keyword);

        // 해당하는 웹툰이 없는 경우
        if(webtoons != null || webtoons.isEmpty()) {
            throw new EntityNotFoundException("해당하는 키워드의 웹툰이 없습니다.");
        }

        List<WebtoonListResponseDto> dtos = new ArrayList<>();

        for (Webtoon webtoon : webtoons) {
            dtos.add(WebtoonListResponseDto.builder()
                    .id(webtoon.getId())
                    .title(webtoon.getTitle())
                    .artist(webtoon.getArtist())
                    .category(webtoon.getCategory())
                    .keyword(webtoon.getKeyword())
                    .build());
        }
        return dtos;
    }

    /**
     * 등록 웹툰 개별 상세 조회
     */
    public WebtoonResponseDto getOneWebtoon(Long webtoonId) {
        Webtoon findWebtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 웹툰입니다."));

        List<WebtoonEpisodeDto> episodeDtos = findWebtoon.getWebtoonDts().stream()
                .map(webtoonDt -> WebtoonEpisodeDto.builder()
                        .episodeNum(webtoonDt.getEpisodeNum())
                        .subTitle(webtoonDt.getSubTitle())
                        .uploadDate(LocalDate.now())
                        .finalUploadYN(false).build())
                .collect(Collectors.toList());


        return WebtoonResponseDto.builder()
                .title(findWebtoon.getTitle())
                .artist(findWebtoon.getArtist())
                .intro(findWebtoon.getIntro())
                .category(findWebtoon.getCategory())
                .keyword(findWebtoon.getKeyword())
                .thumbnailFileName(findWebtoon.getFiles().get(-1).getFileName())   // 저장된 썸네일 파일 중 가장 마지막 썸네일 파일
                .episode(episodeDtos).build();
    }


    /**
     * 등록 웹툰 삭제?
     */
}
