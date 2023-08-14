package com.erp.webtoon.service;

import com.erp.webtoon.domain.Pay;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.pay.PayRequestDto;
import com.erp.webtoon.repository.PayRepository;
import com.erp.webtoon.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PayServiceTest {
    @Autowired
    private PayRepository payRepository;

    @Autowired
    private PayService payService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("급여 등록")
    void saveTest() {
        //given
        //회원 먼저 등록
        User user = User.builder()
                .employeeId("2000")
                .build();

        userRepository.save(user);

        PayRequestDto dto = new PayRequestDto();
        dto.setEmployeeId("2000");
        dto.setYearSalary(100000);
        dto.setAddSalary(20000);
        dto.setPayDate(LocalDate.now());
        dto.toEntity();

        //when
        payService.save(dto);

        //then
        assertEquals(1L, userRepository.count());
        assertEquals(1L, payRepository.count());

        Pay pay = payRepository.findAll().get(0);
        assertEquals(20000, pay.getAddPay());
        assertEquals(100000, pay.getSalary());
    }
}