package com.erp.webtoon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Webtoon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "webtoon_id")
    private Long id;

    private String title;

    private String intro; // 웹툰 소개

    private String artist;  //작가 이름

    private String illustrator; // 그림 작가 이름

    private String category;

    private String keyword;

    @OneToMany(mappedBy = "webtoon", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();   // 썸네일 파일들

    @OneToMany(mappedBy = "webtoon", cascade = CascadeType.ALL)
    private List<WebtoonDt> webtoonDts = new ArrayList<>();     // 해당 웹툰의 각 회차

    @Builder
    public Webtoon(String title, String intro, String artist, String illustrator, String category, String keyword) {
        this.title = title;
        this.intro = intro;
        this.artist = artist;
        this.illustrator = illustrator;
        this.category = category;
        this.keyword = keyword;
    }

    //웹툰 정보 업데이트
    public void updateInfo(String title, String intro, String artist, String illustrator) {
        this.title = title;
        this.intro = intro;
        this.artist = artist;
        this.illustrator = illustrator;
    }
}
