package com.erp.webtoon.domain;

import lombok.Builder;
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

    private LocalDateTime attendTime;  // 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Attendence(String attendDate, String attendType, LocalDateTime attendTime, User user) {
        this.attendDate = attendDate;
        this.attendType = attendType;
        this.attendTime = attendTime;
        this.user = user;
    }
}
