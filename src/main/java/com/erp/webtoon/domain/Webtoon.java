package com.erp.webtoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    private String category;

    private String keyword;

    @OneToMany(mappedBy = "webtoon", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();   // 썸네일 피알들

    @OneToMany(mappedBy = "webtoon", cascade = CascadeType.ALL)
    private List<WebtoonDt> webtoonDts = new ArrayList<>();

}
