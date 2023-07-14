package com.erp.webtoon.service;

import com.erp.webtoon.domain.Pay;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.pay.PayRequestDto;
import com.erp.webtoon.repository.PayRepository;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

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
     * 급여 조회 -> 이때 직원 정보도 같이 조회해야함
     */
    public void search() {

    }

    /**
     * 급여 정보 수정
     */
    public void update() {

    }

    /**
     * 급여 지급여부 수정
     */

}
