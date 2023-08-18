package com.erp.webtoon.domain;

import com.slack.api.methods.request.calls.CallsAddRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private char stat;

    private LocalDateTime regDate; // 작성일시

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();   // 참조된 파일들

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_tpl_id")
    private DocumentTpl documentTpl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "user_id" , name = "write_user_id")
    private User writeUser;   // 작성자

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<DocumentRcv> documentRcvs = new ArrayList<>();   // 수신자 목록

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<DocumentData> documentDataList = new ArrayList<>();   // 데이터 목록

    @Builder
    public Document(String title, String content, char stat, LocalDateTime regDate, DocumentTpl documentTpl, User writeUser) {
        this.title = title;
        this.content = content;
        this.stat = stat;
        this.regDate = regDate;
        this.documentTpl = documentTpl;
        this.writeUser = writeUser;
    }


    //수신자 목록의 이름 불러오기
    public List<String> getRcvNames() {
        return documentRcvs.stream()
                .map(documentRcv -> documentRcv.getUser().getName())
                .collect(Collectors.toList());
    }

    public void changeStat(char stat) { this.stat = stat; }
}
