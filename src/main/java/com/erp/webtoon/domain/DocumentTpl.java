package com.erp.webtoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class DocumentTpl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_tpl_id")
    private Long id;

    private String templateName;    // 템플릿 이름

    private String intro; // 설명

    private String template;    // 템플릿

    @OneToMany(mappedBy = "documentTpl", cascade = CascadeType.ALL)
    private List<Document> documents = new ArrayList<>();   // 문서들
}
