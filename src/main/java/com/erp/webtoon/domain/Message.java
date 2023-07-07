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

    private Long refId; // 참조 ID

    private char stat; // 상태값

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pgm_id")
    private Program program;    // 참조 프로그램ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "user_id" , name = "rcv_user_id")
    private User rcvUser;   // 수신자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "user_id", name = "send_user_id")
    private User sendUser;    // 발신자
}
