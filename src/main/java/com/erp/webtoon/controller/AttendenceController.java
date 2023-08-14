package com.erp.webtoon.controller;

import com.erp.webtoon.dto.attendece.AttendenceRequestDto;
import com.erp.webtoon.dto.attendece.AttendenceResponseDto;
import com.erp.webtoon.dto.attendece.TotalAttendenceResponseDto;
import com.erp.webtoon.service.AttendenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AttendenceController {

    private final AttendenceService attendenceService;

    /*
        출근 & 퇴근
     */
    @PostMapping("/attendence")
    public void attendenceAdd(@RequestBody AttendenceRequestDto dto) throws IOException {
        attendenceService.addAttendence(dto);
    }

    /*
        개인 근태 조회
     */
    @PostMapping("/attendence/individual/{employeeId}")
    public AttendenceResponseDto individualAttendence(@PathVariable("employeeId") String employeeId){
        return attendenceService.getIndividualAttendence(employeeId);
    }

    /*
        전체 근태 조회
     */
    @GetMapping("/attendence/total")
    public TotalAttendenceResponseDto totalAttendence() {
        return attendenceService.getTotalAttendence();
    }

}
