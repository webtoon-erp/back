package com.erp.webtoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    private String noticeType;

    private String title;

    private String content;

    private int readCount;  // 조회수

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();   // 공지사항에 여러개 첨부파일 목록

}
