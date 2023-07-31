package com.erp.webtoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Qualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qlfc_id")
    private Long id;

    private Integer sortSequence; // 정렬번호

    private String qlfcType;    // 자격증 타입

    private String content;     // 내용 (자격증 상세)

    private LocalDate qlfcDate;    // 만료일자

    private int qlfcPay;    // 자격수당

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;      // 해당 자격증 가지고 있는 유저

    @OneToMany(mappedBy = "qualification", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();      // 첨부한 파일목록

    public void updateQlfcPay(int qlfcPay) {
        this.qlfcPay = qlfcPay;
    }
}
