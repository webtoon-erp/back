package com.erp.webtoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class WebtoonDt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "webtoon_dt_id")
    private Long id;

    private String episodeNum;  // 회차번호

    private String subTitle; // 소제목

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id")
    private Webtoon webtoon;    // 웹툰

    @OneToMany(mappedBy = "webtoonDt", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();   // 참조하는 첨부파일들

}
