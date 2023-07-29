package com.erp.webtoon.dto.pay;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PayResponseDto {

    private PayUserResponseDto userInfo; // 개인 정보

    private PayMonthDto monthPay; // 월 지급액

    private List<PayListResponseDto> payList; //지급 목록

    private List<PayQualificationDto> qualificationList; // 자격 수당 리스트
}
