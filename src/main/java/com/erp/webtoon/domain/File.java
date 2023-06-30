package com.erp.webtoon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    private String fileName; // -> 필요할까? -> 필요함 만약 저장한 파일의 이름이 같은경우 중복되기 때문에 고유 경로가 필요

    private String originName;

    private String ext; // 파일 확장자

    private Long fileSize;

    private boolean stat;   // 상태값

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;  // 해당 파일이 첨보된 공지사항

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id")
    private Webtoon webtoon;    // 해당 파일이 첨부된 웹툰

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_dt_id")
    private WebtoonDt webtoonDt;    // 해당 파일이 첨부된 웹툰 디테일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "req_id")
    private Request request;    // 해당 파일이 첨부된 서비스요청

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id")
    private Document document;    // 해당 파일이 첨부된 문서

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qlfc_id")
    private Qualification qualification;    // 해당 파일이 첨부된 자격증

    @Builder
    public File(String fileName, String originName, String ext, Long fileSize) {
        this.fileName = fileName;
        this.originName = originName;
        this.ext = ext;
        this.fileSize = fileSize;
        this.stat = true;
    }

    public void changeStat() {
        this.stat = false;
    }
}
