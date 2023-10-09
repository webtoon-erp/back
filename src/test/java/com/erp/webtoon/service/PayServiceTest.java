package com.erp.webtoon.service;

import com.erp.webtoon.domain.Pay;
import com.erp.webtoon.domain.Qualification;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.pay.*;
import com.erp.webtoon.dto.user.QualificationRequestDto;
import com.erp.webtoon.repository.PayRepository;
import com.erp.webtoon.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PayServiceTest {
    @Autowired
    private PayRepository payRepository;

    @Autowired
    private PayService payService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  UserService userService;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
        payRepository.deleteAll();
    }

    @Test
    @DisplayName("월 급여 등록")
    void save1() {
        //given
        User user = User.builder()
                .employeeId("2000")
                .build();

        userRepository.save(user);

        PayRequestDto dto = new PayRequestDto();
        dto.setEmployeeId("2000");
        dto.setYearSalary(100000);
        dto.setAddSalary(20000);
        dto.setBankAccount("000-000-000-000");
        dto.setPayDate(LocalDate.now());

        //when
        payService.save(dto);

        //then
        assertEquals(1L, payRepository.count());
        Pay pay = payRepository.findAll().get(0);
        assertEquals(20000, pay.getAddPay());
        assertEquals(100000, pay.getSalary());
        assertEquals("000-000-000-000", pay.getBankAccount());
        assertEquals(LocalDate.now(), pay.getPayDate());
    }

    @Test
    @DisplayName("개인 급여 상세 조회")
    void test2() throws MalformedURLException {
        //given
        User user = User.builder()
                .employeeId("2000")
                .name("규규")
                .email("kkk@naver.com")
                .deptName("인사과")
                .build();

        List<Qualification> qualList = new ArrayList<>();
        Qualification qualification = Qualification.builder()
                .qlfcType("정처기")
                .content(null)
                .qlfcDate(LocalDate.now())
                .qlfcPay(50000)
                .user(user)
                .build();
        qualList.add(qualification);
        user.registerQualification(qualList);

        userRepository.save(user);

        PayRequestDto dto = new PayRequestDto();
        dto.setEmployeeId("2000");
        dto.setYearSalary(100000);
        dto.setAddSalary(20000);
        dto.setBankAccount("000-000-000-000");
        dto.setPayDate(LocalDate.now());

        payService.save(dto);

        //when
        PayResponseDto searchDto = payService.search("2000");

        //then
        assertEquals("규규", searchDto.getUserInfo().getName());
        assertEquals("kkk@naver.com", searchDto.getUserInfo().getEmail());
        assertEquals("인사과", searchDto.getUserInfo().getDeptName());
        assertEquals("000-000-000-000", searchDto.getMonthPay().getBankAccount());
        assertEquals(100000, searchDto.getMonthPay().getYearSalary());
        assertEquals(8333, searchDto.getMonthPay().getMonthSalary());
        assertEquals(50000, searchDto.getMonthPay().getQualSalary());
        PayQualificationDto payQualificationDto = searchDto.getQualificationList().get(0);
        assertEquals("정처기", payQualificationDto.getName());
        assertEquals(50000, payQualificationDto.getMoney());
    }

    @Test
    @DisplayName("전제 급여 조회")
    void test3() {
        //given
        List<User> userList = IntStream.range(1, 6)
                .mapToObj(i -> User.builder()
                        .employeeId("100" + i)
                        .name("규규" + i).build())
                .collect(Collectors.toList());


        userRepository.saveAll(userList);

        PayRequestDto dto1 = new PayRequestDto();
        dto1.setEmployeeId("1001");
        dto1.setYearSalary(100000);
        dto1.setAddSalary(20000);
        dto1.setBankAccount("000-000-000-111");
        dto1.setPayDate(LocalDate.now());

        PayRequestDto dto2 = new PayRequestDto();
        dto2.setEmployeeId("1003");
        dto2.setYearSalary(7000);
        dto2.setAddSalary(10000);
        dto2.setBankAccount("000-000-000-333");
        dto2.setPayDate(LocalDate.now());

        payService.save(dto1);
        payService.save(dto2);

        //when
        List<PayAllListResponseDto> payAllListResponseDtos = payService.allPayList();

        //then
        assertEquals(5L, payAllListResponseDtos.size());
        assertEquals("규규1", payAllListResponseDtos.get(0).getName());
        assertEquals(8333, payAllListResponseDtos.get(0).getMonthPay());
        assertEquals("규규3", payAllListResponseDtos.get(2).getName());
        assertEquals(583, payAllListResponseDtos.get(2).getMonthPay());
    }

    @Test
    @DisplayName("월 급여 수정")
    void test4() {
        //given
        User user = User.builder()
                .employeeId("2000")
                .build();

        userRepository.save(user);

        PayRequestDto dto = new PayRequestDto();
        dto.setEmployeeId("2000");
        dto.setYearSalary(100000);
        dto.setAddSalary(20000);
        dto.setBankAccount("000-000-000-000");
        dto.setPayDate(LocalDate.of(2020, 8, 10));

        payService.save(dto);

        PayMonthUpdateDto payMonthUpdateDto = new PayMonthUpdateDto();
        payMonthUpdateDto.setYearSalary(200000);
        payMonthUpdateDto.setAddSalary(50000);
        payMonthUpdateDto.setPayDate(LocalDate.now());

        //when
        payService.updateMonthPay("2000", payMonthUpdateDto);

        //then
        assertEquals(1L, payRepository.count());
        Pay pay = payRepository.findAll().get(0);
        assertEquals(50000, pay.getAddPay());
        assertEquals(200000, pay.getSalary());
        assertEquals("000-000-000-000", pay.getBankAccount());
        assertEquals(LocalDate.now(), pay.getPayDate());
    }

    @Test
    @DisplayName("계좌 수정")
    void test5() {
        //given
        User user = User.builder()
                .employeeId("2000")
                .build();

        userRepository.save(user);

        PayRequestDto dto = new PayRequestDto();
        dto.setEmployeeId("2000");
        dto.setYearSalary(100000);
        dto.setAddSalary(20000);
        dto.setBankAccount("000-000-000-000");
        dto.setPayDate(LocalDate.now());

        payService.save(dto);

        PayAccountUpdateDto payAccountUpdateDto = new PayAccountUpdateDto();
        payAccountUpdateDto.setBankAccount("000-000-000-111");

        //when
        payService.updateAccount("2000", payAccountUpdateDto);

        //then
        assertEquals(1L, payRepository.count());
        Pay pay = payRepository.findAll().get(0);
        assertEquals(20000, pay.getAddPay());
        assertEquals(100000, pay.getSalary());
        assertEquals("000-000-000-111", pay.getBankAccount());
        assertEquals(LocalDate.now(), pay.getPayDate());
    }

    @Test
    @DisplayName("지급일 여러명 수정")
    void test6() {
        //given
        List<User> userList = IntStream.range(1, 6)
                .mapToObj(i -> User.builder()
                        .employeeId("100" + i)
                        .deptName("부서" + i)
                        .teamNum(i)
                        .name("규규" + i).build())
                .collect(Collectors.toList());
        userRepository.saveAll(userList);

        PayRequestDto dto1 = new PayRequestDto();
        dto1.setEmployeeId("1001");
        dto1.setYearSalary(100000);
        dto1.setAddSalary(20000);
        dto1.setBankAccount("000-000-000-111");
        dto1.setPayDate(LocalDate.of(2023, 8, 10));

        PayRequestDto dto2 = new PayRequestDto();
        dto2.setEmployeeId("1003");
        dto2.setYearSalary(7000);
        dto2.setAddSalary(10000);
        dto2.setBankAccount("000-000-000-333");
        dto2.setPayDate(LocalDate.of(2023, 7, 15));

        payService.save(dto1);
        payService.save(dto2);

        List<PayDateUpdateListDto> dtos = new ArrayList<>();

        PayDateUpdateListDto update1 = new PayDateUpdateListDto();
        update1.setEmployeeId("1001");
        update1.setPayDate(LocalDate.now());

        PayDateUpdateListDto update2 = new PayDateUpdateListDto();
        update2.setEmployeeId("1003");
        update2.setPayDate(LocalDate.now());

        dtos.add(update1);
        dtos.add(update2);

        //when
        payService.updateAllPayDate(dtos);

        //then
        assertEquals(5L, userRepository.count());
        assertEquals(LocalDate.now(), payRepository.findAll().get(0).getPayDate());
        assertEquals(LocalDate.now(), payRepository.findAll().get(1).getPayDate());
    }

    @Test
    @DisplayName("자격 수당 수정")
    void test7() throws MalformedURLException {
        //given
        User user = User.builder()
                .employeeId("2000")
                .name("규규")
                .deptName("인사과")
                .build();

        List<Qualification> qualList = new ArrayList<>();
        Qualification qualification1 = Qualification.builder()
                .qlfcType("정처기")
                .content(null)
                .qlfcPay(0)
                .qlfcDate(LocalDate.now())
                .user(user)
                .build();
        qualList.add(qualification1);
        Qualification qualification2 = Qualification.builder()
                .qlfcType("토익")
                .content(null)
                .qlfcPay(0)
                .qlfcDate(LocalDate.now())
                .user(user)
                .build();
        qualList.add(qualification2);
        user.registerQualification(qualList);

        userRepository.save(user);

        PayRequestDto dto = new PayRequestDto();
        dto.setEmployeeId("2000");
        dto.setYearSalary(100000);
        dto.setAddSalary(20000);
        dto.setBankAccount("000-000-000-000");
        dto.setPayDate(LocalDate.now());

        payService.save(dto);

        List<QualificationPayRequestDto> dtos = new ArrayList<>();

        QualificationPayRequestDto update1 = new QualificationPayRequestDto();
        update1.setQualId(qualification1.getId());
        update1.setQualPay(50000);

        QualificationPayRequestDto update2 = new QualificationPayRequestDto();
        update2.setQualId(qualification2.getId());
        update2.setQualPay(10000);

        dtos.add(update1);
        dtos.add(update2);

        //when
        payService.saveQualPay(dtos);
        PayResponseDto searchDto = payService.search("2000");

        //then
        assertEquals(60000, searchDto.getMonthPay().getQualSalary());
        PayQualificationDto payQualificationDto1 = searchDto.getQualificationList().get(0);
        PayQualificationDto payQualificationDto2= searchDto.getQualificationList().get(1);
        assertEquals("정처기", payQualificationDto1.getName());
        assertEquals(50000, payQualificationDto1.getMoney());
        assertEquals("토익", payQualificationDto2.getName());
        assertEquals(10000, payQualificationDto2.getMoney());
    }

    @Test
    @DisplayName("급여 지급 여부 수정")
    void test8() {
        //given
        User user = User.builder()
                .employeeId("2000")
                .build();

        userRepository.save(user);

        PayRequestDto dto = new PayRequestDto();
        dto.setEmployeeId("2000");
        dto.setYearSalary(100000);
        dto.setAddSalary(20000);
        dto.setBankAccount("000-000-000-000");
        dto.setPayDate(LocalDate.now());

        payService.save(dto);

        //when
        payService.update(payRepository.findAll().get(0).getId());

        //then
        assertEquals(1L, payRepository.count());
        Pay pay = payRepository.findAll().get(0);
        assertEquals(true, pay.isPayYN());
    }
}
