package com.erp.webtoon.controller;


import com.erp.webtoon.dto.itsm.RequestListResponseDto;
import com.erp.webtoon.dto.itsm.RequestResponseDto;
import com.erp.webtoon.dto.itsm.RequestStepDto;
import com.erp.webtoon.dto.itsm.CommentListDto;
import com.erp.webtoon.dto.itsm.CommentResponseDto;
import com.erp.webtoon.dto.itsm.RequestDeleteDto;
import com.erp.webtoon.dto.itsm.RequestDto;
import com.erp.webtoon.dto.itsm.RequestRegisterResponseDto;
import com.erp.webtoon.dto.message.MessageSaveDto;
import com.erp.webtoon.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/purchase-request")
    public RequestRegisterResponseDto purchaseRequest(@RequestBody RequestDto requestDto) throws Exception {
        return requestService.purchaseRequest(requestDto);
    }

    @PostMapping("/request")
    public RequestRegisterResponseDto Request(@RequestBody RequestDto requestDto) throws Exception {
        return requestService.assistRequest(requestDto);
    }

    @DeleteMapping("/request")
    public ResponseEntity deleteRequest(@RequestBody List<RequestDeleteDto> requestIds) {
        requestService.deleteRequest(requestIds);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping ("/comment")
    public CommentResponseDto registerComment(@RequestBody MessageSaveDto messageSaveDto) throws IOException {
        return requestService.registerComment(messageSaveDto);
    }

    @GetMapping("/comment")
    public List<CommentListDto> getAllComments(@RequestParam("requestId") Long requestId){
        return requestService.getAllComments(requestId);
    }

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
    @GetMapping("/request/list/{employeeId}")
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
    @PutMapping("/request/step/{requestId}")
    public ResponseEntity changeStep(@PathVariable Long requestId, @RequestBody RequestStepDto dto) {
        requestService.changeStep(requestId, dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/comment")
    public ResponseEntity deleteComment(@RequestParam("messageId") Long messageId){
        requestService.deleteComment(messageId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
