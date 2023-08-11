package com.erp.webtoon.dto.attendece;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TotalAttendenceSummaryDto {

    private Long totalUserCnt;      // 전체 직원수

    private Long onTimeStartUserCnt;      // 정상 출근 직원수

    private Long lateStartUserCnt;       // 지각 출근 직원수

    private Long notStartUserCnt;  // 미출근 직원수

    private Long dayOffUserCnt;     // 휴가 직원수

    private Long onTimeEndUserCnt;        // 정상 퇴근 직원수

    private Long notEndUserCnt;       // 연장근무 직원수er

}
