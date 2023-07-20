package com.erp.webtoon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private LocalDate noticeDate;

    private int readCount = 0;  // 조회수

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();   // 공지사항에 여러개 첨부파일 목록

    @Builder
    public Notice(String noticeType, String title, String content) {
        this.noticeType = noticeType;
        this.title = title;
        this.content = content;
    }

    public void addReadCount() {
        this.readCount++;
    }

    //공지사항에 저장된 파일 목록의 저장명 불러오기
    public List<String> getFileNames() {
        return files.stream()
                .map(file -> file.getOriginName())
                .collect(Collectors.toList());
    }

    public void updateNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
    public void updateContent(String content) {
        this.content = content;
    }
}
