package com.erp.webtoon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Pay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id")
    private Long id;

    private int salary; //얀봉

    private int addPay; //추가 수당

    private String bankAccount;

    private LocalDate payDate; // 가장 최근(과거) 지급일

    private boolean payYN; // 지급 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Pay(int salary, int addPay, String bankAccount, LocalDate payDate, boolean payYN, User user) {
        this.salary = salary;
        this.addPay = addPay;
        this.bankAccount = bankAccount;
        this.payDate = payDate;
        this.payYN = payYN;
        this.user = user;
    }

    // 지급여부 수정
    public void updatePayYN() {
        if (this.payYN == false) {
            this.payYN = true;
        }
    }

    //지급 수정
    public void updatePay(int salary, int addPay, LocalDate payDate) {
        this.salary = salary;
        this.addPay = addPay;
        this.payDate = payDate;
    }

    //계좌 수정
    public void updateAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    //지급날짜 수정
    public void updatePayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    // 연관관계 메소드
    public void setUserPay(User user) {
        this.user = user;
        user.getPays().add(this);
    }
}
