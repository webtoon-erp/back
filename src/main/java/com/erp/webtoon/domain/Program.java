package com.erp.webtoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Program {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pgm_id")
    private Long id;

    private String programId; // 프로그램 ID : 프론트단에서 사용

    private String programName; // 프로그램 이름

    private Long serviceId; // 서비스아이디

    private String serviceName; // 서비스 이름

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<Auth> authList = new ArrayList<>();    //해당 프로그램이 가진 권한 목록
}
