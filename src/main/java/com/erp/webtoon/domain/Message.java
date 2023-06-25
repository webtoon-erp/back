package com.erp.webtoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Message {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "msg_id")
    private Long id;

    private String msgType;     // 메세지 타입

    private String content;     // 메세지 내용

    private int refId; // 참조 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pgm_id")
    private Program program;    // 참조 프로그램ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // 메세지 수신 유저
}
