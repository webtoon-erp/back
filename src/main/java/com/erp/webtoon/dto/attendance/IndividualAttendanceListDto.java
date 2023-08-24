package com.erp.webtoon.dto.attendance;

import java.time.LocalDateTime;

public interface IndividualAttendanceListDto {

    int getWeek(); // 주차

    String getAttendDate();  //  기준일

    LocalDateTime getStartTime();  // 시작시간

    LocalDateTime getEndTime();  // 종료시간

    String getTotalTime(); // 총 근무시간

}
