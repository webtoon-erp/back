package com.erp.webtoon.dto.attendance;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IndividualAttendanceListDto {

    private int week; // 주차

    private String attendDate;  //  기준일

    private LocalDateTime startTime;  // 시작시간

    private LocalDateTime endTime;  // 종료시간

    private int totalTime; // 총 근무시간

}
