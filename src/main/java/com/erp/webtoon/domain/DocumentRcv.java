package com.erp.webtoon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class DocumentRcv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_rcv_id")
    private Long id;

    private int sortSequence; // 정렬번호

    private String receiveType;

    private boolean stat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id")
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public DocumentRcv (int sortSequence, String receiveType, boolean stat, Document document, User user) {
        this.sortSequence = sortSequence;
        this.receiveType = receiveType;
        this.stat = stat;
        this.document = document;
        this.user = user;
    }

    public void changeStat(boolean stat) { this.stat = stat; }
}
