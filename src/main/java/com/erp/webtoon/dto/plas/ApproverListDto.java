package com.erp.webtoon.dto.plas;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApproverListDto {

    private String name;    // 이름

    private String deptName;    //부서명

    private String position;    // 직급

    private String employeeId; // 사번

}
