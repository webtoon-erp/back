package com.erp.webtoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class DocumentData {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_data_id")
    private Long id;

    private int sortSequence;   // 정렬 번호
    private String content; // 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id")
    private Document document;



}
