package com.erp.webtoon.service;

import com.erp.webtoon.domain.Attendence;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.attendece.*;
import com.erp.webtoon.repository.AttendenceRepository;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        전체 근태 조회
     */
    public TotalAttendenceResponseDto getTotalAttendence() {
        return TotalAttendenceResponseDto.builder()
                .totalAttendenceSummaryDto(getTotalAttendenceSummary())
                .monthlyOvertimeSummaryDto(getMonthlyOvertime())
                .departmentOvertimeSumDto(getDeptOverTimeSum())
                .departmentOvertimeAvgDto(getDeptOverTimeAvg())
                .build();
    }

    // 전체 근태 - 현황 요약 조회
    private TotalAttendenceSummaryDto getTotalAttendenceSummary() {

        return TotalAttendenceSummaryDto.builder()
                .totalUserCnt(userRepository.countAllBy())
                .onTimeStartUserCnt((Long) getOnTimeStartAttendances().get("count"))
                .lateStartUserCnt((Long) getLateStartAttendances().get("count"))
                .notStartUserCnt(countNotStartAttendances())
                .dayOffUserCnt((Long) getDayOffAttendances().get("count"))
                .onTimeEndUserCnt((Long) getOnTimeEndAttendances().get("count"))
                .notEndUserCnt(countNotEndAttendances())
                .build();

    }

    // 전체 근태 - 부서별 연장근무 시간 합계 조회
    private DepartmentOvertimeSumDto getDeptOverTimeSum() {

        return DepartmentOvertimeSumDto.builder()
                .hrOvertimeSum(getOverTimeSumByDepartment("HR"))
                .amOvertimeSum(getOverTimeSumByDepartment("AM"))
                .wtOvertimeSum(getOverTimeSumByDepartment("WT"))
                .itOVertimeSum(getOverTimeSumByDepartment("IT"))
                .build();

    }

    // 전체 근태 - 부서별 연장근무 시간 평균 조회
    private DepartmentOvertimeAvgDto getDeptOverTimeAvg() {

        return DepartmentOvertimeAvgDto.builder()
                .hrOvertimeAvg(getOverTimeAvgByDepartment("HR"))
                .amOvertimeAvg(getOverTimeAvgByDepartment("AM"))
                .wtOvertimeAvg(getOverTimeAvgByDepartment("WT"))
                .itOVertimeAvg(getOverTimeAvgByDepartment("IT"))
                .build();

    }


    // 전체 근태 - 월별 연장근무 시간 조회
    private MonthlyOvertimeSummaryDto getMonthlyOvertime() {
        return MonthlyOvertimeSummaryDto.builder()
                .janOvertime(calculateOvertimeByMonth(1))
                .febOvertime(calculateOvertimeByMonth(2))
                .marOvertime(calculateOvertimeByMonth(3))
                .aprOvertime(calculateOvertimeByMonth(4))
                .mayOvertime(calculateOvertimeByMonth(5))
                .junOvertime(calculateOvertimeByMonth(6))
                .junOvertime(calculateOvertimeByMonth(7))
                .augOvertime(calculateOvertimeByMonth(8))
                .sepOvertime(calculateOvertimeByMonth(9))
                .octOvertime(calculateOvertimeByMonth(10))
                .novOvertime(calculateOvertimeByMonth(11))
                .devOvertime(calculateOvertimeByMonth(12))
                .build();
    }

    // 전체 - 정시 출근 직원 수
    private Map<String, Object> getOnTimeStartAttendances() {
        String currentDate = LocalDate.now().toString();
        String attendType = "START";

        List<Attendence> attendances = attendenceRepository.findByAttendDateAndAttendType(currentDate, attendType);

        Map<String, Object> results = new HashMap<>();

        long count = attendances.stream()
                        .filter(this::isOnTime)
                        .count();

        List<User> userList = attendances.stream()
                                .filter(this::isOnTime)
                                .map(Attendence::getUser)
                                .collect(Collectors.toList());

        results.put("count", count);
        results.put("userList", userList);

        return results;
    }

    // 전체 - 지각 출근 직원 수
    private Map<String, Object> getLateStartAttendances() {
        String currentDate = LocalDate.now().toString();
        String attendType = "START";

        List<Attendence> attendances = attendenceRepository.findByAttendDateAndAttendType(currentDate, attendType);

        Map<String, Object> results = new HashMap<>();

        long count = attendances.stream()
                        .filter(attendance -> !isOnTime(attendance))
                        .count();

        List<User> userList = attendances.stream()
                                .filter(attendance -> !isOnTime(attendance))
                                .map(Attendence::getUser)
                                .collect(Collectors.toList());

        results.put("count", count);
        results.put("userList", userList);

        return results;
    }

    // 전체 - 휴가 직원 수
    private Map<String, Object> getDayOffAttendances() {
        String currentDate = LocalDate.now().toString();

        List<Attendence> attendances = attendenceRepository.findByAttendDateAndAttendType(currentDate, "DAYOFF");

        Map<String, Object> results = new HashMap<>();

        results.put("count", attendances.size());
        results.put("userList", attendances.stream().map(Attendence::getUser).collect(Collectors.toList()));

        return results;
    }

    // 전체 - 퇴근 직원 수
    private Map<String, Object> getOnTimeEndAttendances() {
        String currentDate = LocalDate.now().toString();

        List<Attendence> attendances = attendenceRepository.findByAttendDateAndAttendType(currentDate, "END");

        Map<String, Object> results = new HashMap<>();

        results.put("count", attendances.size());
        results.put("userList", attendances.stream().map(Attendence::getUser).collect(Collectors.toList()));

        return results;
    }

    // 전체 - 연장 근무 (미퇴근) 직원 수
    private Long countNotEndAttendances() {
        String currentDate = LocalDate.now().toString();

        // 출근한 직원 수 (지각 포함)
        long startAttendances = attendenceRepository.findByAttendDateAndAttendType(currentDate, "START").size();

        // 퇴근한 직원 수
        long endAttendances = attendenceRepository.findByAttendDateAndAttendType(currentDate, "END").size();

        // 현재 시간 구하기
        LocalTime currentTime = LocalTime.now();

        if (currentTime.isAfter(LocalTime.of(18, 10)))  return startAttendances - endAttendances;
        else  return 0L;

    }

    // 전체 - 미출근 직원 수
    private Long countNotStartAttendances() {
        String currentDate = LocalDate.now().toString();

        long startAttendances = 0;
        long dayOffAttendances = 0;

        // 전체 직원 수
        long allEmployees = userRepository.countAllBy();

        // 출근한 직원 수 (지각 포함)
        List<Attendence> startAttendancesList = attendenceRepository.findByAttendDateAndAttendType(currentDate, "START");
        if (startAttendancesList != null)  startAttendances = startAttendancesList.size();

        // 휴가인 직원 수
        List<Attendence> dayOffAttendencesList = attendenceRepository.findByAttendDateAndAttendType(currentDate, "DAYOFF");
        if (dayOffAttendencesList != null)  dayOffAttendances = dayOffAttendencesList.size();

        return allEmployees - startAttendances - dayOffAttendances;
    }

    // 정시 출근 판단 함수 - 실제 출근 시간이 9시 10분 이전이면 true
    private boolean isOnTime(Attendence attendence) {
        LocalDateTime expectedStartTime = LocalDate.parse(attendence.getAttendDate()).atTime(9, 10);
        LocalDateTime actualStartTime = attendence.getAttendTime();

        return actualStartTime.isBefore(expectedStartTime);
    }

    // 이번달 부서별 연장 근무 합계 시간
    private String getOverTimeSumByDepartment(String deptCode) {

        int currentMonth = LocalDate.now().getMonthValue();
        String attendType = "END";
        List<User> userList = userRepository.findAllByDeptCode(deptCode);

        List<Attendence> attendances = attendenceRepository.findByAttendMonthAndAttendTypeAndUserIn(currentMonth, attendType, userList);


        return sumOvertime(attendances);
    }

    // 이번달 부서별 연장 근무 시간 평균 계산
    private String getOverTimeAvgByDepartment(String deptCode) {

        int currentMonth = LocalDate.now().getMonthValue();
        String attendType = "END";

        List<User> userList = userRepository.findAllByDeptCode(deptCode);
        List<Attendence> attendances = attendenceRepository.findByAttendMonthAndAttendTypeAndUserIn(currentMonth, attendType, userList);

        if (attendances.isEmpty())  return "00:00:00";

        Duration totalOverTime = attendances.stream()
                .map(this::calculateOverTime)
                .reduce(Duration.ZERO, Duration::plus);

        long totalMinutes = totalOverTime.toMinutes();
        int numEmployees = userList.size();

        long averageMinutes = totalMinutes / numEmployees;

        long avgHours = averageMinutes / 60;
        long avgMinutesRemaining = averageMinutes % 60;

        return String.format("%02d:%02d:00", avgHours, avgMinutesRemaining);
    }

    // 월별 연장근무 시간
    private String calculateOvertimeByMonth(int month) {
        List<Attendence> attendences = attendenceRepository.findByAttendMonthAndAttendType(month, "END");

        return sumOvertime(attendences);
    }

    // 연장근무 시간 계산
    private Duration calculateOverTime(Attendence attendence) {
        LocalDateTime expectedEndTime = LocalDate.parse(attendence.getAttendDate()).atTime(18, 0);
        LocalDateTime actualEndTime = attendence.getAttendTime();

        return Duration.between(expectedEndTime, actualEndTime);
    }

    // 연장근무 시간 합계
    private String sumOvertime(List<Attendence> attendances) {

        if (attendances.isEmpty())  return "00:00:00";

        Duration totalOverTime = attendances.stream()
                .map(this::calculateOverTime)
                .reduce(Duration.ZERO, Duration::plus);

        long totalHours = totalOverTime.toHours();
        long totalMinutes = totalOverTime.toMinutesPart();
        long totalSeconds = totalOverTime.toSecondsPart();

        return String.format("%02d:%02d:%02d", totalHours, totalMinutes, totalSeconds);

    }
}
