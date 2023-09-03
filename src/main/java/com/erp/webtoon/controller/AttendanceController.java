package com.erp.webtoon.controller;

import com.erp.webtoon.dto.attendance.AttendanceRequestDto;
import com.erp.webtoon.dto.attendance.AttendanceResponseDto;
import com.erp.webtoon.dto.attendance.TotalAttendanceResponseDto;
import com.erp.webtoon.dto.common.ErrorResponseDto;
import com.erp.webtoon.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    /**
        출근 & 퇴근
     */
    @PostMapping
    public ResponseEntity addAttendanceRecord(@RequestBody AttendanceRequestDto dto) throws IOException {
        try {
            attendanceService.addAttendance(dto);
            return ResponseEntity.ok().build();
        }
        catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    /**
        개인 근태 조회
     */
    @GetMapping("/{employeeId}")
    public AttendanceResponseDto getIndividualAttendance(@PathVariable String employeeId) {
        return attendanceService.getIndividualAttendance(employeeId);
    }

    /**
        전체 근태 조회
     */
    @GetMapping("/total")
    public TotalAttendanceResponseDto getTotalAttendance() {
        return attendanceService.getTotalAttendance();
    }

}
