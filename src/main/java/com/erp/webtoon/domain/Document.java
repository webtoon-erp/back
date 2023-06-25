package com.erp.webtoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private Long id;

    private String title;

    private String content;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();   // 참조된 파일들

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_tpl_id")
    private DocumentTpl documentTpl;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<DocumentRcv> documentRcvs = new ArrayList<>();   // 수신자 목록

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<DocumentData> documentDataList = new ArrayList<>();   // 데이터 목록
}
