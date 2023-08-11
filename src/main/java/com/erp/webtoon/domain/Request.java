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


    //단계 변경 메소드
    public void changeStep(int step) {
        this.step = step;
    }

    @Builder
    public Request(Long id, String reqType, String title, String content, int step, LocalDate dueDate, LocalDate doneDate, User reqUser, User itUser, List<File> files) {
        this.id = id;
        this.reqType = reqType;
        this.title = title;
        this.content = content;
        this.step = step;
        this.dueDate = dueDate;
        this.doneDate = doneDate;
        this.reqUser = reqUser;
        this.itUser = itUser;
        this.files = files;
    }

    public void addRequestDt(RequestDt requestDt) {
        requestDts.add(requestDt);
        requestDt.setRequest(this);
    }
}