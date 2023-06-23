package com.erp.webtoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Auth {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    private Long id;

    private String deptCode;

    private String teamNo;

    private String position;

    private boolean searchYN;

    private boolean saveYN;

    private boolean updateYN;

    private boolean deleteYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pgm_id")
    private Program program;
}
