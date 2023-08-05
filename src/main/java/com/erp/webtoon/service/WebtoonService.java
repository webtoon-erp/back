package com.erp.webtoon.service;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.domain.Webtoon;
import com.erp.webtoon.dto.webtoon.WebtoonRequestDto;
import com.erp.webtoon.dto.webtoon.WebtoonResponseDto;
import com.erp.webtoon.dto.webtoon.WebtoonDtListDto;
import com.erp.webtoon.dto.webtoon.WebtoonListResponseDto;
import com.erp.webtoon.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;
    private final FileService fileService;

    /**
     * 등록 웹툰 생성
     */
    @Transactional
    public Long save(WebtoonRequestDto dto) throws IOException {
        Webtoon webtoon = dto.toEntity();

        if(!dto.getThumbnailFile().isEmpty()) {
            File uploadfile = fileService.save(dto.getThumbnailFile());
            uploadfile.updateFileWebtoon(webtoon);
            webtoon.getFiles().add(uploadfile);
        }

        webtoonRepository.save(webtoon);
        return webtoon.getId();
    }

    /**
     * 등록 웹툰 전체 조회 (List)
     */
    public List<WebtoonListResponseDto> getAllWebtoon() {

        List<WebtoonListResponseDto> webtoonList = webtoonRepository.findAll(Sort.by("category")).stream()
                .map(WebtoonListResponseDto::new)
                .collect(Collectors.toList());

        return webtoonList;
    }

    /**
     * 웹툰 카드뷰 조회 -> 임시와 최종 나눠서 각각 6개씩
     * 주차별로 나눠야 함...주차를 뭘로 판단하지..
     */


    /**
     * 등록 웹툰 검색 조회 -> 제목 / 작가 / 카테고리 / 키워드 별
     */

    //제목 검색
    public List<WebtoonListResponseDto> getTitleWebtoon(String title) {
        List<Webtoon> webtoons = webtoonRepository.findByTitle(title);

        // 해당하는 웹툰이 없는 경우
        if(webtoons == null || webtoons.isEmpty()) {
            throw new EntityNotFoundException("해당하는 제목의 웹툰이 없습니다.");
        }

        List<WebtoonListResponseDto> webtoonList = webtoons.stream()
                .map(WebtoonListResponseDto::new)
                .collect(Collectors.toList());

        return webtoonList;
    }

    //작가 검색
    public List<WebtoonListResponseDto> getArtistWebtoon(String artist) {
        List<Webtoon> webtoons = webtoonRepository.findByArtist(artist);

        // 해당하는 웹툰이 없는 경우
        if(webtoons == null || webtoons.isEmpty()) {
            throw new EntityNotFoundException("해당하는 작가의 웹툰이 없습니다.");
        }

        List<WebtoonListResponseDto> webtoonList = webtoons.stream()
                .map(WebtoonListResponseDto::new)
                .collect(Collectors.toList());

        return webtoonList;
    }

    //카테고리 검색
    public List<WebtoonListResponseDto> getCategoryWebtoon(String category) {
        List<Webtoon> webtoons = webtoonRepository.findByCategory(category);

        // 해당하는 웹툰이 없는 경우
        if(webtoons == null || webtoons.isEmpty()) {
            throw new EntityNotFoundException("해당하는 카테고리의 웹툰이 없습니다.");
        }

        List<WebtoonListResponseDto> webtoonList = webtoons.stream()
                .map(WebtoonListResponseDto::new)
                .collect(Collectors.toList());

        return webtoonList;
    }

    // 키워드 검색
    public List<WebtoonListResponseDto> getKeywordWebtoon(String keyword) {
        List<Webtoon> webtoons = webtoonRepository.findByKeyword(keyword);

        // 해당하는 웹툰이 없는 경우
        if(webtoons == null || webtoons.isEmpty()) {
            throw new EntityNotFoundException("해당하는 키워드의 웹툰이 없습니다.");
        }

        List<WebtoonListResponseDto> webtoonList = webtoons.stream()
                .map(WebtoonListResponseDto::new)
                .collect(Collectors.toList());

        return webtoonList;
    }

    /**
     * 등록 웹툰 개별 상세 조회
     */
    public WebtoonResponseDto getOneWebtoon(Long webtoonId) {
        Webtoon findWebtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 웹툰입니다."));

        List<WebtoonDtListDto> episodeDtos = findWebtoon.getWebtoonDts().stream()
                .map(webtoonDt -> WebtoonDtListDto.builder()
                        .episodeNum(webtoonDt.getEpisodeNum())
                        .subTitle(webtoonDt.getSubTitle())
                        .uploadDate(LocalDate.now())
                        .manager(webtoonDt.getManager())
                        .finalUploadYN(false).build())
                .collect(Collectors.toList());


        return WebtoonResponseDto.builder()
                .title(findWebtoon.getTitle())
                .artist(findWebtoon.getArtist())
                .illustrator(findWebtoon.getIllustrator())
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
