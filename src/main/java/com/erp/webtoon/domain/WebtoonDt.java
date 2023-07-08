package com.erp.webtoon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
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

    private LocalDate uploadDate; // 업로드 날짜

    private boolean finalUploadYN; // 임시 업로드(false) or 최종 업로드 여부(true)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id")
    private Webtoon webtoon;    // 웹툰

    @OneToMany(mappedBy = "webtoonDt", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();   // 참조하는 첨부파일들

    @Builder
    public WebtoonDt(String episodeNum, String subTitle, Webtoon webtoon) {
        this.episodeNum = episodeNum;
        this.subTitle = subTitle;
        this.uploadDate = LocalDate.now();
        this.finalUploadYN = false;
        this.webtoon = webtoon;
    }

    public void changeUploadState() {
        this.finalUploadYN = true;
        this.uploadDate = LocalDate.now();
    }

    public void updateInfo(String episodeNum, String subTitle) {
        this.episodeNum = episodeNum;
        this.subTitle = subTitle;
    }
}
