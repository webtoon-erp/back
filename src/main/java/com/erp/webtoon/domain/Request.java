package com.erp.webtoon.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

import javax.inject.Singleton;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "req_id")
    private Long id;

    private String reqType;

    private String title;

    private String content;

    private int step; // 1: 요청 2: 접수 3: 진행 4: 완료 5: 반려

    private LocalDate dueDate;     // 기한 일자

    private LocalDate doneDate;    // 완료 일자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "user_id", name = "req_user_id")
    private User reqUser;   // 요청자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "user_id", name = "it_user_id")
    private User itUser;    // 담당자

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();   // 요청에 쓰이는 첨부파일들

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    private List<RequestDt> requestDts = new ArrayList<>();   // 요청에 여러가지 다테일내용들
}