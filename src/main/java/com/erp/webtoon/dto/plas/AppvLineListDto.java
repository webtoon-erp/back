package com.erp.webtoon.dto.plas;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppvLineListDto {

    private String name;    // 이름

    private String deptName;    //부서명

    private int teamNum;    // 팀 번호

    private String position;    // 직급

}
