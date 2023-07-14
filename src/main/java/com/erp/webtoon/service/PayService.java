package com.erp.webtoon.service;

import com.erp.webtoon.domain.Pay;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.pay.PayListResponseDto;
import com.erp.webtoon.dto.pay.PayRequestDto;
import com.erp.webtoon.dto.pay.PayResponseDto;
import com.erp.webtoon.repository.PayRepository;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PayService {

    private final PayRepository payRepository;

    private final UserRepository userRepository;

    /**
     * 급여 등록
     */
    public Long save(PayRequestDto dto) {

        User findUser = userRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

        Pay newPay = dto.toEntity(findUser);

        findUser.getPays().add(newPay);

        payRepository.save(newPay);
        return newPay.getId();
    }

    /**
     * 급여 조회 -> 이때 직원 정보도 같이 조회해야함 + 급여 list
     */
    public PayResponseDto search(String employeeId) {
        User findUser = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));


        List<PayListResponseDto> payList = findUser.getPays().stream()
                .map(pay -> PayListResponseDto.builder()
                        .totalMonthSalary((pay.getSalary() / 12) + pay.getAddPay())
                        .addSalary(pay.getAddPay())
                        .payDate(pay.getPayDate())
                        .payYN(pay.isPayYN()).build())
                .collect(Collectors.toList());

        // 가장 최근의 급여정보
        Pay recentPay = findUser.getPays().get(-1);

        return PayResponseDto.builder()
                .employeeId(findUser.getEmployeeId())
                .name(findUser.getName())
                .email(findUser.getEmail())
                .deptName(findUser.getDeptName())
                .position(findUser.getPosition())
                .bankAccount(recentPay.getBankAccount())
                .yearSalary(recentPay.getSalary())
                .monthSalary(recentPay.getSalary() / 12)
                .payList(payList).build();
    }


    /**
     * 급여 지급여부 수정
     */
    public boolean update(Long payId) {
        Pay findPay = payRepository.findById(payId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 급여입니다."));

        findPay.updatePayYN();
        return true;
    }

}
