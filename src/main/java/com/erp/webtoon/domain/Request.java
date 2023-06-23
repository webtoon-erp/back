package com.erp.webtoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "req_id")
    private Long id;

    private String reqType;

    private String title;

    private String content;

    private int step;

    private String dueDate;     // 기한 일자

    private String doneDate;    // 완료 일자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "user_id" , name = "req_user_id")
    private User reqUser;   // 요청자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "user_id", name = "it_user_id")
    private User itUser;    // 담당자

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();   // 요청에 쓰이는 첨부파일들

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    private List<RequestDt> requestDts = new ArrayList<>();   // 요청에 여러가지 다테일내용들
}
