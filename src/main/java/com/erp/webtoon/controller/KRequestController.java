package com.erp.webtoon.controller;

import com.erp.webtoon.dto.itsm.RequestListResponseDto;
import com.erp.webtoon.dto.itsm.RequestResponseDto;
import com.erp.webtoon.dto.itsm.RequestStepDto;
import com.erp.webtoon.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KRequestController {

    private final RequestService requestService;

    /**
     * 요청 상세 조회
     */
    @GetMapping("/request/{requestId}")
    public ResponseEntity showOne(@PathVariable Long requestId) {
        RequestResponseDto request = requestService.search(requestId);

        return ResponseEntity.ok(request);
    }

    /**
     * 사원 별 과거 요청 리스트 조회
     */
    @GetMapping("/request/{employeeId}")
    public ResponseEntity showUserReqList(@PathVariable String employeeId) {
        List<RequestListResponseDto> requestList = requestService.searchUserList(employeeId);
        return ResponseEntity.ok(requestList);
    }

    /**
     * IT팀 서비스 요청 전체 리스트 조회
     */
    @GetMapping("/request/all/{employeeId}")
    public ResponseEntity showAllList(@PathVariable String employeeId) throws IllegalAccessException {
        List<RequestListResponseDto> requestList = requestService.searchAllList(employeeId);
        return ResponseEntity.ok(requestList);
    }


    /**
     * 단계 변경
     */
    @PostMapping("/request/step/{requestId}")
    public ResponseEntity changeStep(@PathVariable Long requestId, @RequestBody RequestStepDto dto) {
        requestService.changeStep(requestId, dto);
        return new ResponseEntity(HttpStatus.OK);
    }

}
