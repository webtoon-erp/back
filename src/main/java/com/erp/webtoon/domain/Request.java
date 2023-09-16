package com.erp.webtoon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    //연관관계 매소드
    public void updateUserRequest() {
        this.reqUser.getRequests().add(this);
        this.itUser.getItRequests().add(this);
    }

    @Builder
    public Request(Long id, String reqType, String title, String content, int step, LocalDate dueDate, LocalDate doneDate, User reqUser, User itUser) {
        this.id = id;
        this.reqType = reqType;
        this.title = title;
        this.content = content;
        this.step = step;
        this.dueDate = dueDate;
        this.doneDate = doneDate;
        this.reqUser = reqUser;
        this.itUser = itUser;
    }
}