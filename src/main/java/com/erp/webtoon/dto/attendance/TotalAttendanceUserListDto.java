package com.erp.webtoon.dto.attendance;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TotalAttendanceUserListDto {

    private String deptName;    //부서명
    private int teamNum;        // 팀 번호
    private String position;    // 직급
    private String employeeId;  // 사번
    private String name;        // 이름
    private String tel;         // 전화번호
    private String email;       // 이메일

}
