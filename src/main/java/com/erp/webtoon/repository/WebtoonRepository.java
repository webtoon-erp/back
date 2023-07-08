package com.erp.webtoon.repository;

import com.erp.webtoon.domain.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {
    List<Webtoon> findByTitle(String title);    // 제목 검색
    List<Webtoon> findByArtist(String artist);  // 작가 검색
    List<Webtoon> findByCategory(String category);  // 무슨요일 웹툰인지 카테고리 별 검색
    List<Webtoon> findByKeyword(String keyword);    // 키워드 검색
}
