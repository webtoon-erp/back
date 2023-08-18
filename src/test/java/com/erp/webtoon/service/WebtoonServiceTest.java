package com.erp.webtoon.service;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.domain.Webtoon;
import com.erp.webtoon.dto.webtoon.WebtoonListResponseDto;
import com.erp.webtoon.dto.webtoon.WebtoonRequestDto;
import com.erp.webtoon.dto.webtoon.WebtoonResponseDto;
import com.erp.webtoon.dto.webtoon.WebtoonUpdaateDto;
import com.erp.webtoon.repository.FileRepository;
import com.erp.webtoon.repository.WebtoonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebtoonServiceTest {

    @Autowired
    private WebtoonService webtoonService;

    @Autowired
    private WebtoonRepository webtoonRepository;

    @Autowired
    private FileRepository fileRepository;

    @BeforeEach
    void clean() {webtoonRepository.deleteAll();}

    @Test
    @DisplayName("웹툰 등록")
    void test1() throws IOException {
        //given
        WebtoonRequestDto dto = new WebtoonRequestDto();
        dto.setTitle("제목입니다.");
        dto.setIntro("인트로입니다.");
        dto.setArtist("작가입니다.");

        //when
        Long id = webtoonService.save(dto, null);

        //then
        assertEquals(1L, webtoonRepository.count());
        Webtoon webtoon = webtoonRepository.findAll().get(0);
        assertEquals(id, webtoon.getId());
        assertEquals("제목입니다.", webtoon.getTitle());
        assertEquals("인트로입니다.", webtoon.getIntro());
        assertEquals("작가입니다.", webtoon.getArtist());
    }

    @Test
    @DisplayName("웹툰 전체 리스트 조회")
    void test2() {
        //given
        List<Webtoon> webtoonList = IntStream.range(1, 22)
                .mapToObj(i -> Webtoon.builder()
                        .title("제목입니다." + i)
                        .artist("작가입니다." + i)
                        .illustrator("그림 작가입니다." + i)
                        .category((i % 7) + "요일")
                        .keyword("카테고리" + i)
                        .build())
                .collect(Collectors.toList());

        webtoonRepository.saveAll(webtoonList);

        //when
        List<WebtoonListResponseDto> allWebtoon = webtoonService.getAllWebtoon();

        //then
        assertEquals(21L, webtoonRepository.count());
        assertEquals("0요일", allWebtoon.get(0).getCategory());
        assertEquals("제목입니다.7", allWebtoon.get(0).getTitle());
        assertEquals("0요일", allWebtoon.get(1).getCategory());
        assertEquals("제목입니다.14", allWebtoon.get(1).getTitle());
        assertEquals("1요일", allWebtoon.get(3).getCategory());
        assertEquals("제목입니다.1", allWebtoon.get(3).getTitle());
    }

    @Test
    @DisplayName("웹툰 개별 상세 조회")
    void test3() {
        //given
        Webtoon newWebtoon = Webtoon.builder()
                .title("제목입니다.")
                .intro("인트로입니다.")
                .category("월요일")
                .build();

        File file = File.builder()
                .fileName("파일명")
                .build();

        file.updateFileWebtoon(newWebtoon);
        newWebtoon.getFiles().add(file);

        webtoonRepository.save(newWebtoon);

        //when
        WebtoonResponseDto oneWebtoon = webtoonService.getOneWebtoon(newWebtoon.getId());

        //then
        assertEquals("제목입니다.", oneWebtoon.getTitle());
        assertEquals("인트로입니다.", oneWebtoon.getIntro());
        assertEquals("월요일", oneWebtoon.getCategory());
        assertEquals("파일명", oneWebtoon.getThumbnailFileName());
        assertEquals(0, oneWebtoon.getEpisode().size());
    }

    @Test
    @DisplayName("웹툰 수정-파일x")
    void test4() throws IOException {
        //given
        Webtoon newWebtoon = Webtoon.builder()
                .title("제목입니다.")
                .intro("인트로입니다.")
                .artist("규규")
                .illustrator("그림 작가")
                .category("월요일")
                .keyword("공포")
                .build();

        webtoonRepository.save(newWebtoon);

        WebtoonUpdaateDto dto = new WebtoonUpdaateDto();
        dto.setTitle("제목입니다.");
        dto.setIntro("인트로입니다.");
        dto.setArtist("규규2");
        dto.setIllustrator("규규2");
        dto.setCategory("월요일");
        dto.setKeyword("공포");


        //when
        webtoonService.update(newWebtoon.getId(), null, dto);

        //then
        Webtoon webtoon = webtoonRepository.findAll().get(0);
        assertEquals("제목입니다.", webtoon.getTitle());
        assertEquals("인트로입니다.", webtoon.getIntro());
        assertEquals("규규2", webtoon.getArtist());
        assertEquals("규규2", webtoon.getIllustrator());
        assertEquals("월요일", webtoon.getCategory());
        assertEquals("공포", webtoon.getKeyword());
    }
}