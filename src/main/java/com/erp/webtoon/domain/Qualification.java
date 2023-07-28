package com.erp.webtoon.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Qualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qlfc_id")
    private Long id;

    private int sortSequence; // 정렬번호

    private String qlfcType;    // 자격증 타입

    private String content;     // 내용

    private LocalDate qlfcDate;    // 만료일자

    private int qlfcPay;    // 자격수당

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;      // 해당 자격증 가지고 있는 유저

    @OneToMany(mappedBy = "qualification", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();      // 첨부한 파일목록
}
