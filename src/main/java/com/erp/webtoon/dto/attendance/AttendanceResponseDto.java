package com.erp.webtoon.dto.attendance;

import lombok.Data;

import java.util.List;

@Data
public class AttendanceResponseDto {

    private String weeklyTotalTime; // 이번주 누적 근무 시간

    private String weeklyOverTime; // 이번주 초과 근무 시간

    private String monthlyTotalTime; // 이번달 누적 근무 시간

    private String monthlyOverTime; // 이번달 초과 근무 시간

    private List<IndividualAttendanceListDto> attendanceList; // 개인 근태 목록

}
