package com.erp.webtoon.service;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.domain.Pay;
import com.erp.webtoon.domain.Qualification;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.pay.PayAccountUpdateDto;
import com.erp.webtoon.dto.pay.PayAllListResponseDto;
import com.erp.webtoon.dto.pay.PayDateUpdateListDto;
import com.erp.webtoon.dto.pay.PayListResponseDto;
import com.erp.webtoon.dto.pay.PayMonthDto;
import com.erp.webtoon.dto.pay.PayMonthUpdateDto;
import com.erp.webtoon.dto.pay.PayQualificationDto;
import com.erp.webtoon.dto.pay.PayRequestDto;
import com.erp.webtoon.dto.pay.PayResponseDto;
import com.erp.webtoon.dto.pay.PayUserResponseDto;
import com.erp.webtoon.dto.pay.QualificationPayRequestDto;
import com.erp.webtoon.repository.PayRepository;
import com.erp.webtoon.repository.QualificationRepository;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PayService {

    private final PayRepository payRepository;
    private final UserRepository userRepository;
    private final QualificationRepository qualificationRepository;

    private final FileService fileService;

    /**
     * 월 급여 등록
     */
    @Transactional
    public void save(PayRequestDto dto) {

        User findUser = userRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

        Pay newPay = dto.toEntity();

        newPay.setUserPay(findUser);

        payRepository.save(newPay);
    }

    /**
     * 급여 조회 -> 이때 직원 정보도 같이 조회해야함 + 급여 list
     */
    public PayResponseDto search(String employeeId) throws MalformedURLException {
        User findUser = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

        File userFile = findUser.getFile();
        String fullPath = "";
        if (userFile != null) {
            fullPath = fileService.getFullPath(userFile.getFileName());
        }

        //유저 정보
        PayUserResponseDto userInfoDto = new PayUserResponseDto(findUser, fullPath);


        //자격 수당 리스트
        List<PayQualificationDto> qualificationDtos = findUser.getQualifications().stream()
                .map(PayQualificationDto::new)
                .collect(Collectors.toList());

        //이번달 급여 정보
        if (findUser.getPays().isEmpty()) {
            return PayResponseDto.builder()
                    .userInfo(userInfoDto)
                    .monthPay(null)
                    .payList(null)
                    .qualificationList(qualificationDtos)
                    .build();
        }

        // 가장 최근의 급여 정보
        PayMonthDto payMonthDto = new PayMonthDto(findUser.getPays().get(findUser.getPays().size() - 1));
        payMonthDto.setQualSalary(findUser.getQualifications());

        //해당 유저의 지금까지 받았던 지급 정보
        List<PayListResponseDto> payList = findUser.getPays().stream()
                .filter(pay -> pay.isPayYN() == true)
                .map(PayListResponseDto::new)
                .collect(Collectors.toList());

        return PayResponseDto.builder()
                .userInfo(userInfoDto)
                .monthPay(payMonthDto)
                .payList(payList)
                .qualificationList(qualificationDtos)
                .build();

    }

    /**
     * 전체 급여 리스트
     */
    public List<PayAllListResponseDto> allPayList() {
        List<PayAllListResponseDto> payList = new ArrayList<>();

        List<User> allUser = userRepository.findAll();

        for (User user : allUser) {

            PayAllListResponseDto dto = new PayAllListResponseDto(user);

            if(!user.getPays().isEmpty()) {
                dto.addPayInfo(user.getPays().get(user.getPays().size()-1));
            }
            payList.add(dto);
        }
        return payList;
    }

    /**
     * 월 급여 수정
     */
    @Transactional
    public void updateMonthPay(String employeeId, PayMonthUpdateDto dto) {
        User findUser = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 직원입니다."));

        findUser.getPays().get(findUser.getPays().size()-1).updatePay(dto.getYearSalary(), dto.getAddSalary(), dto.getPayDate());
    }

    /**
     * 계좌 수정
     */
    @Transactional
    public void updateAccount(String employeeId, PayAccountUpdateDto dto) {
        User findUser = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 직원입니다."));

        findUser.getPays().get(findUser.getPays().size()-1).updateAccount(dto.getBankAccount());
    }

    /**
     * 지급일 여러명 수정
     */
    @Transactional
    public void updateAllPayDate(List<PayDateUpdateListDto> dtos) {
        for (PayDateUpdateListDto dto : dtos) {
            User findUser = userRepository.findByEmployeeId(dto.getEmployeeId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사원입니다."));

            findUser.getPays().get(findUser.getPays().size()-1).updatePayDate(dto.getPayDate());
        }
    }

    /**
     * 자격 수당 수정
     */
    @Transactional
    public void saveQualPay(List<QualificationPayRequestDto> dtos) {
        for (QualificationPayRequestDto dto : dtos) {
            Qualification findQual = qualificationRepository.findById(dto.getQualId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 자격증입니다."));

            findQual.updateQlfcPay(dto.getQualPay());
        }
    }

    /**
     * 급여 지급여부 수정
     */
    @Transactional
    public boolean update(Long payId) {
        Pay findPay = payRepository.findById(payId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 급여입니다."));

        findPay.updatePayYN();
        return true;
    }
}