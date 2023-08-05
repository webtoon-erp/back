package com.erp.webtoon.dto.attendece;

import lombok.Data;

@Data
public class TotalAttendenceSummaryDto {

    private Long totalUserCnt;      // 전체 직원수

    private Long startUserCnt;      // 정상 출근 직원수

    private Long lateUserCnt;       // 지각 출근 직원수

    private Long nonAttendUserCnt;  // 미출근 직원수

    private Long dayOffUserCnt;     // 휴가 직원수

    private Long endUserCnt;        // 정상 퇴근 직원수

    private Long overUserCnt;       // 연장근무 직원수

}
