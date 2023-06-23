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

    private String programName;

    private Long serviceId;

    private String serviceName;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<Auth> authList = new ArrayList<>();

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();
}
