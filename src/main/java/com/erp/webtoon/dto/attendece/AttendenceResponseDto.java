package com.erp.webtoon.dto.attendece;

import lombok.Data;

import java.util.List;

@Data
public class AttendenceResponseDto {

    private int weeklyTotalTime; // 이번주 누적 근무 시간

    private int weeklyOverTime; // 이번주 초과 근무 시간

    private int monthlyTotalTime; // 이번달 누적 근무 시간

    private int monthlyOverTime; // 이번달 초과 근무 시간

    private List<IndividualAttenedenceListDto> attendenceList; // 개인 근태 목록

}
