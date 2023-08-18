package com.erp.webtoon.service;

import com.erp.webtoon.domain.Attendance;
import com.erp.webtoon.domain.User;
import com.erp.webtoon.dto.attendance.*;
import com.erp.webtoon.repository.AttendanceRepository;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    /*
        출근 & 퇴근
     */
    @Transactional
    public void addAttendance(AttendanceRequestDto dto) throws IOException {

        User user = userRepository.findByEmployeeId(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("해당 직원의 정보가 존재하지 않습니다."));

        Attendance attendance = dto.toEntity(user);
        attendanceRepository.save(attendance);

    }

    /*
        개인 근태 조회
     */
    public AttendanceResponseDto getIndividualAttendance(String employeeId) {

        User user = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 직원의 정보가 존재하지 않습니다."));

        AttendanceResponseDto dto = new AttendanceResponseDto();

        dto.setWeeklyTotalTime(attendanceRepository.findIndividualWeeklyTotalTime(user.getId()));
        dto.setWeeklyOverTime(attendanceRepository.findIndividualWeeklyOverTime(user.getId()));
        dto.setMonthlyTotalTime(attendanceRepository.findIndividualMonthlyTotalTime(user.getId()));
        dto.setMonthlyOverTime(attendanceRepository.findIndividualMonthlyOverTime(user.getId()));
        dto.setAttendanceList( attendanceRepository.findIndividualAttendance(user));

        return dto;

    }

    /*
        전체 근태 조회
     */
    public TotalAttendanceResponseDto getTotalAttendance() {
        return TotalAttendanceResponseDto.builder()
                .totalAttendanceSummaryDto(getTotalAttendanceSummary())
                .monthlyOvertimeSummaryDto(getMonthlyOvertime())
                .departmentOvertimeSumDto(getDeptOverTimeSum())
                .departmentOvertimeAvgDto(getDeptOverTimeAvg())
                .build();
    }

    // 전체 근태 - 현황 요약 조회
    private TotalAttendanceSummaryDto getTotalAttendanceSummary() {

        return TotalAttendanceSummaryDto.builder()
                .totalUserCnt(userRepository.countAllBy())
                .onTimeStartUserCnt(getOnTimeStartAttendances().getCount())
                .lateStartUserCnt(getLateStartAttendances().getCount())
                .notStartUserCnt(getNotStartAttendances().getCount())
                .dayOffUserCnt(getDayOffAttendances().getCount())
                .onTimeEndUserCnt(getOnTimeEndAttendances().getCount())
                .notEndUserCnt(getNotEndAttendances().getCount())
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

    // 근태 결과 클래스
    private static class AttendanceResult {
        private long count;
        private List<TotalAttendanceUserListDto> userList;

        public AttendanceResult() {
            this.count = 0;
            this.userList = Collections.emptyList();
        }

        public AttendanceResult(long count, List<TotalAttendanceUserListDto> userList) {
            this.count = count;
            this.userList = userList;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public List<TotalAttendanceUserListDto> getUserList() {
            return userList;
        }

        public void setUserList(List<TotalAttendanceUserListDto> userList) {
            this.userList = userList;
        }

    }

    // 전체 - 정시 출근 직원 수
    private AttendanceResult getOnTimeStartAttendances() {
        String currentDate = LocalDate.now().toString();
        String attendType = "START";

        List<Attendance> attendances = attendanceRepository.findByAttendDateAndAttendType(currentDate, attendType);

        long count = attendances.stream()
                        .filter(this::isOnTime)
                        .count();

        List<User> userList = attendances.stream()
                                .filter(this::isOnTime)
                                .map(Attendance::getUser)
                                .collect(Collectors.toList());

        return new AttendanceResult(count, userListDtoList(userList));
    }

    // 전체 - 지각 출근 직원 수
    private AttendanceResult getLateStartAttendances() {
        String currentDate = LocalDate.now().toString();
        String attendType = "START";

        List<Attendance> attendances = attendanceRepository.findByAttendDateAndAttendType(currentDate, attendType);

        long count = attendances.stream()
                        .filter(attendance -> !isOnTime(attendance))
                        .count();

        List<User> userList = attendances.stream()
                                .filter(attendance -> !isOnTime(attendance))
                                .map(Attendance::getUser)
                                .collect(Collectors.toList());

        return new AttendanceResult(count, userListDtoList(userList));
    }

    // 전체 - 휴가 직원 수
    private AttendanceResult getDayOffAttendances() {
        String currentDate = LocalDate.now().toString();

        List<Attendance> attendances = attendanceRepository.findByAttendDateAndAttendType(currentDate, "DAYOFF");

        long count = attendances.size();
        List<User> userList = attendances.stream().map(Attendance::getUser).collect(Collectors.toList());

        return new AttendanceResult(count, userListDtoList(userList));
    }

    // 전체 - 퇴근 직원 수
    private AttendanceResult getOnTimeEndAttendances() {
        String currentDate = LocalDate.now().toString();

        List<Attendance> attendances = attendanceRepository.findByAttendDateAndAttendType(currentDate, "END");

        long count = attendances.size();
        List<User> userList = attendances.stream().map(Attendance::getUser).collect(Collectors.toList());

        return new AttendanceResult(count, userListDtoList(userList));
    }

    // 전체 - 연장 근무 (미퇴근) 직원 수
    private AttendanceResult getNotEndAttendances() {
        String currentDate = LocalDate.now().toString();
        LocalTime currentTime = LocalTime.now();

        if (currentTime.isAfter(LocalTime.of(18, 10))) {
            // 출근한 직원 (지각 포함)
            List<Attendance> startAttendances = attendanceRepository.findByAttendDateAndAttendType(currentDate, "START");
            if (startAttendances == null)  return new AttendanceResult();
            List<User> startAttendancesUserList = startAttendances.stream().map(Attendance::getUser).collect(Collectors.toList());

            long count = startAttendances.size() - getOnTimeEndAttendances().getCount();

            List<TotalAttendanceUserListDto> userList = new ArrayList<>(userListDtoList(startAttendancesUserList));
            userList.removeAll(getOnTimeEndAttendances().getUserList());

            return new AttendanceResult(count, userList);
        }
        else {
            return new AttendanceResult();
        }
    }

    // 전체 - 미출근 직원 수
    private AttendanceResult getNotStartAttendances() {
        String currentDate = LocalDate.now().toString();

        // 전체 직원 수
        List<User> allUserList = userRepository.findAll();

        // 출근한 직원 수 (지각 포함)
        List<Attendance> startAttendances = attendanceRepository.findByAttendDateAndAttendType(currentDate, "START");
        if (startAttendances == null)  return new AttendanceResult(allUserList.size(), userListDtoList(allUserList));
        List<User> startAttendancesUserList = startAttendances.stream().map(Attendance::getUser).collect(Collectors.toList());

        long count = allUserList.size() - startAttendances.size() - getDayOffAttendances().getCount();

        List<TotalAttendanceUserListDto> userList = new ArrayList<>(userListDtoList(allUserList));
        userList.removeAll(userListDtoList(startAttendancesUserList));
        userList.removeAll(getDayOffAttendances().getUserList());

        return new AttendanceResult(count, userList);
    }

    // 정시 출근 판단 함수 - 실제 출근 시간이 9시 10분 이전이면 true
    private boolean isOnTime(Attendance attendance) {
        LocalDateTime expectedStartTime = LocalDate.parse(attendance.getAttendDate()).atTime(9, 10);
        LocalDateTime actualStartTime = attendance.getAttendTime();

        return actualStartTime.isBefore(expectedStartTime);
    }

    // 이번달 부서별 연장 근무 합계 시간
    private String getOverTimeSumByDepartment(String deptCode) {

        int currentMonth = LocalDate.now().getMonthValue();
        String attendType = "END";
        List<User> userList = userRepository.findAllByDeptCode(deptCode);

        List<Attendance> attendances = attendanceRepository.findByAttendMonthAndAttendTypeAndUserIn(currentMonth, attendType, userList);


        return sumOvertime(attendances);
    }

    // 이번달 부서별 연장 근무 시간 평균 계산
    private String getOverTimeAvgByDepartment(String deptCode) {

        int currentMonth = LocalDate.now().getMonthValue();
        String attendType = "END";

        List<User> userList = userRepository.findAllByDeptCode(deptCode);
        List<Attendance> attendances = attendanceRepository.findByAttendMonthAndAttendTypeAndUserIn(currentMonth, attendType, userList);

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
        List<Attendance> attendances = attendanceRepository.findByAttendMonthAndAttendType(month, "END");

        return sumOvertime(attendances);
    }

    // 연장근무 시간 계산
    private Duration calculateOverTime(Attendance attendance) {
        LocalDateTime expectedEndTime = LocalDate.parse(attendance.getAttendDate()).atTime(18, 0);
        LocalDateTime actualEndTime = attendance.getAttendTime();

        return Duration.between(expectedEndTime, actualEndTime);
    }

    // 연장근무 시간 합계
    private String sumOvertime(List<Attendance> attendances) {

        if (attendances.isEmpty())  return "00:00:00";

        Duration totalOverTime = attendances.stream()
                .map(this::calculateOverTime)
                .reduce(Duration.ZERO, Duration::plus);

        long totalHours = totalOverTime.toHours();
        long totalMinutes = totalOverTime.toMinutesPart();
        long totalSeconds = totalOverTime.toSecondsPart();

        return String.format("%02d:%02d:%02d", totalHours, totalMinutes, totalSeconds);

    }

    // USER -> TotalAttendanceUserListDto
    private List<TotalAttendanceUserListDto> userListDtoList (List<User> userList) {

        return userList.stream()
                .map(user -> TotalAttendanceUserListDto.builder()
                        .deptName(user.getDeptName())
                        .teamNum(user.getTeamNum())
                        .position(user.getPosition())
                        .name(user.getName())
                        .tel(user.getTel())
                        .email(user.getEmail())
                        .build())
                .collect(Collectors.toList());
    }

}