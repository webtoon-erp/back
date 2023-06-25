package com.erp.webtoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    private String fileName;

    private String originName;

    private String ext; // 파일 확장자

    private int fileSize;

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
}
