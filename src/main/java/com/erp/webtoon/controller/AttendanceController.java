package com.erp.webtoon.controller;

import com.erp.webtoon.dto.attendance.AttendanceRequestDto;
import com.erp.webtoon.dto.attendance.AttendanceResponseDto;
import com.erp.webtoon.dto.attendance.TotalAttendanceResponseDto;
import com.erp.webtoon.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    /*
        출근 & 퇴근
     */
    @PostMapping("/attendance")
    public void attendanceAdd(@RequestBody AttendanceRequestDto dto) throws IOException {
        attendanceService.addAttendance(dto);
    }

    /*
        개인 근태 조회
     */
    @PostMapping("/attendance/individual/{employeeId}")
    public AttendanceResponseDto individualAttendance(@PathVariable("employeeId") String employeeId){
        return attendanceService.getIndividualAttendance(employeeId);
    }

    /*
        전체 근태 조회
     */
    @GetMapping("/attendance/total")
    public TotalAttendanceResponseDto totalAttendance() {
        return attendanceService.getTotalAttendance();
    }

}
