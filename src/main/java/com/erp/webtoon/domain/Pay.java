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

    private int salary;

    private int addPay; //추가 수당

    private String bankAccount;

    private LocalDate payDate; // 지급일

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
}
