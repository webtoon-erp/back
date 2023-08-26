package com.erp.webtoon.domain;

import lombok.Builder;
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

    private String qlfcType;    // 자격증 타입

    private String content;     // 내용 (자격증 상세)

    private LocalDate qlfcDate;    // 만료일자

    private int qlfcPay;    // 자격수당

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;      // 해당 자격증 가지고 있는 유저

    public void updateQlfcPay(int qlfcPay) {
        this.qlfcPay = qlfcPay;
    }

    @Builder
    public Qualification(Long id, String qlfcType, String content, LocalDate qlfcDate, int qlfcPay, User user) {
        this.id = id;
        this.qlfcType = qlfcType;
        this.content = content;
        this.qlfcDate = qlfcDate;
        this.qlfcPay = qlfcPay;
        this.user = user;
    }

}
