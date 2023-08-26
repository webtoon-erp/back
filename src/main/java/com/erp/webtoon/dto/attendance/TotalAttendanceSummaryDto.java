package com.erp.webtoon.dto.attendance;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TotalAttendanceSummaryDto {

    private Long totalUserCnt;          // 전체 직원수

    private List<TotalAttendanceUserListDto> totalUserList;       // 전체 직원 리스트

    private Long onTimeStartUserCnt;    // 정상 출근 직원수

    private List<TotalAttendanceUserListDto> onTimeStartUserList; // 정상 출근 직원 리스트

    private Long lateStartUserCnt;      // 지각 출근 직원수

    private List<TotalAttendanceUserListDto> lateStartUserList;   // 지각 출근 직원 리스트

    private Long notStartUserCnt;       // 미출근 직원수

    private List<TotalAttendanceUserListDto> notStartUserList;    // 미출근 직원 리스트

    private Long dayOffUserCnt;         // 휴가 직원수

    private List<TotalAttendanceUserListDto> dayOffUserList;      // 휴가 직원 리스트

    private Long onTimeEndUserCnt;      // 정상 퇴근 직원수

    private List<TotalAttendanceUserListDto> onTimeEndUserList;   // 정상 퇴근 직원 리스트

    private Long notEndUserCnt;         // 연장근무 직원수

    private List<TotalAttendanceUserListDto> notEndUserList;      // 연장근무 직원 리스트

}
