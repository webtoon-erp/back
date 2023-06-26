package com.erp.webtoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Attendence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "atd_id")
    private Long id;

    private String attendDate;  //  기준일

    private String attendType;  // 근태타입

    private LocalDateTime inTime;  // 출근시간

    private LocalDateTime outTime; // 퇴근 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
