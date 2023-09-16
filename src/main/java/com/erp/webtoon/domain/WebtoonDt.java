package com.erp.webtoon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    private int episodeNum;  // 회차번호

    private String subTitle; // 소제목

    private String content; // 작가의 말

    private String manager; // 담당자 이름

    private LocalDate uploadDate; // 업로드 날짜

    private boolean finalUploadYN; // 임시 업로드(false) or 최종 업로드 여부(true)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id")
    private Webtoon webtoon;    // 웹툰

    @OneToMany(mappedBy = "webtoonDt", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();   // 참조하는 첨부파일들

    @Builder
    public WebtoonDt(String subTitle, String content) {
        this.subTitle = subTitle;
        this.content = content;
        this.uploadDate = LocalDate.now();
        this.finalUploadYN = false;
    }

    public void changeUploadState() {
        this.finalUploadYN = true;
        this.uploadDate = LocalDate.now();
    }

    public void updateInfo(String subTitle, String content, String manager) {
        this.subTitle = subTitle;
        this.content = content;
        this.manager = manager;
        this.uploadDate = LocalDate.now();
    }

    //연관관계 메서드
    public void setWebtoon(Webtoon webtoon) {
        this.webtoon = webtoon;
        webtoon.getWebtoonDts().add(this);
    }

    // 회차번호 자동 증가
    public void setEpisodeNum(int num) {
        this.episodeNum = num + 1;
    }

    // 담당자 등록
    public void setManager(String name) {
        this.manager = name;
    }
}
