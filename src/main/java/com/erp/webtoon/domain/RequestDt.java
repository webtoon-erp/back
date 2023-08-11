package com.erp.webtoon.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "req_dt_id")
    private Long id;

    private int sortSequence; // 정렬번호

    private String content; // 상세 내용

    private int count;

    private int cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "req_id")
    private Request request;

    public void setRequest(Request request) {
        this.request = request;
        request.getRequestDts().add(this);
    }
}
