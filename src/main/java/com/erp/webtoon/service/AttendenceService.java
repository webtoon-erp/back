package com.erp.webtoon.service;

import com.erp.webtoon.domain.Attendence;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.attendece.AttendenceRequestDto;
import com.erp.webtoon.dto.attendece.AttendenceResponseDto;
import com.erp.webtoon.dto.attendece.TotalAttendenceSummaryDto;
import com.erp.webtoon.repository.AttendenceRepository;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendenceService {

    private final AttendenceRepository attendenceRepository;
    private final UserRepository userRepository;

    /*
        출근 & 퇴근
     */
    @Transactional
    public void addAttendence(AttendenceRequestDto dto) throws IOException {

        User user = userRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("해당 직원의 정보가 존재하지 않습니다."));

        Attendence attendence = dto.toEntity(user);
        attendenceRepository.save(attendence);

    }

    /*
        개인 근태 조회
     */
    public AttendenceResponseDto getIndividualAttendence(String employeeId) {

        User user = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 직원의 정보가 존재하지 않습니다."));

        AttendenceResponseDto dto = new AttendenceResponseDto();

        dto.setWeeklyTotalTime(attendenceRepository.findIndividualWeeklyTotalTime(user.getId()));
        dto.setWeeklyOverTime(attendenceRepository.findIndividualWeeklyOverTime(user.getId()));
        dto.setMonthlyTotalTime(attendenceRepository.findIndividualMonthlyTotalTime(user.getId()));
        dto.setMonthlyOverTime(attendenceRepository.findIndividualMonthlyOverTime(user.getId()));
        dto.setAttendenceList(attendenceRepository.findIndividualAttendence(user));

        return dto;

    }

    /*
        전체 근태 현황 조회
     */
    public TotalAttendenceSummaryDto getTotalAttendence() {

        return TotalAttendenceSummaryDto.builder()
                .totalUserCnt(userRepository.countAllBy())
                .onTimeEndUserCnt(countOnTimeStartAttendances())
                .lateStartUserCnt(countLateStartAttendances())
                .notStartUserCnt(countNotStartAttendances())
                .dayOffUserCnt(countDayOffAttendances())
                .notEndUserCnt(countNotEndAttendances())
                .build();

    }

    // 전체 - 정시 출근 직원 수
    public Long countOnTimeStartAttendances() {
        String currentDate = LocalDate.now().toString();;
        String attendType = "START";

        List<Attendence> attendances = attendenceRepository.findByAttendDateAndAttendType(currentDate, attendType);

        return attendances.stream()
                .filter(this::isOnTime)
                .count();
    }

    // 전체 - 지각 출근 직원 수
    public Long countLateStartAttendances() {
        String currentDate = LocalDate.now().toString();;
        String attendType = "START";

        List<Attendence> attendances = attendenceRepository.findByAttendDateAndAttendType(currentDate, attendType);

        return attendances.stream()
                .filter(attendance -> !isOnTime(attendance))
                .count();
    }

    // 전체 - 휴가 직원 수
    public Long countDayOffAttendances() {
        String currentDate = LocalDate.now().toString();
        String attendType = "DAYOFF";

        List<Attendence> attendances = attendenceRepository.findByAttendDateAndAttendType(currentDate, attendType);

        return (long) attendances.size();
    }

    // 전체 - 퇴근 직원 수
    public Long countOnTimeEndAttendances() {
        String currentDate = LocalDate.now().toString();
        String attendType = "END";

        List<Attendence> attendances = attendenceRepository.findByAttendDateAndAttendType(currentDate, attendType);

        return (long) attendances.size();
    }

    // 전체 - 연장 근무 (미퇴근) 직원 수
    public Long countNotEndAttendances() {
        String currentDate = LocalDate.now().toString();

        // 출근한 직원 수 (지각 포함)
        long startAttendances = attendenceRepository.findByAttendDateAndAttendType(currentDate, "START").size();

        // 퇴근한 직원 수
        long endAttendances = attendenceRepository.findByAttendDateAndAttendType(currentDate, "END").size();

        // 현재 시간 구하기
        LocalTime currentTime = LocalTime.now();

        if (currentTime.isAfter(LocalTime.of(18, 10))) {
            return startAttendances - endAttendances;
        } else {
            return 0L;
        }
    }

    // 전체 - 미출근 직원 수
    public Long countNotStartAttendances() {
        String currentDate = LocalDate.now().toString();

        // 전체 직원 수
        long allEmployees = userRepository.countAllBy();

        // 출근한 직원 수 (지각 포함)
        long startAttendances = attendenceRepository.findByAttendDateAndAttendType(currentDate, "START").size();

        // 휴가인 직원 수
        long dayOffAttendances = attendenceRepository.findByAttendDateAndAttendType(currentDate, "DAYOFF").size();

        return allEmployees - startAttendances - dayOffAttendances;
    }

    // 정시 출근 판단 함수 - 실제 출근 시간이 9시 10분 이전이면 true
    private boolean isOnTime(Attendence attendence) {
        LocalDateTime expectedStartTime = LocalDate.parse(attendence.getAttendDate()).atTime(9, 10);
        LocalDateTime actualStartTime = attendence.getAttendTime();

        return actualStartTime.isBefore(expectedStartTime);
    }


}
