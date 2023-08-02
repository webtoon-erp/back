package com.erp.webtoon.dto.pay;

import com.erp.webtoon.domain.Pay;
import com.erp.webtoon.domain.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PayAllListResponseDto {

    private String name;

    private String employeeId;

    private String deptName;

    private int teamNum;    // 팀 번호

    private int monthPay;

    private LocalDate payDate;

    private boolean payStat; // 지급 상태

    public PayAllListResponseDto(User user) {
        this.name = user.getName();
        this.employeeId = user.getEmployeeId();
        this.deptName = user.getDeptName();
        this.teamNum = user.getTeamNum();
    }

    //급여정보 추가 메소드
    public void addPayInfo(Pay pay) {
        this.monthPay = pay.getSalary() / 12;
        this.payDate = pay.getPayDate();
        this.payStat = pay.isPayYN();
    }
}
