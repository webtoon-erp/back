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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "file_id")
//    private File file;
    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<NoticeFile> noticeFiles = new ArrayList<>();   // 공지사항에 여러개 첨부파일

}
