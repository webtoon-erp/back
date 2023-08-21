package com.erp.webtoon.controller;

import com.erp.webtoon.dto.plas.*;
import com.erp.webtoon.service.PlasService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plas")
public class PlasController {

    private final PlasService plasService;

    // 결재자 / 참조자 조회 기능
    @GetMapping("/doc/appvLineList")
    public List<AppvLineListDto> appvLineList() {
        return plasService.getAppvLineList();
    }

    // 전자결재 문서 저장
    @PostMapping("/doc")
    public void save(@RequestBody DocumentRequestDto dto) throws IOException { plasService.addDoc(dto); }

    // 전자결재 문서 삭제
    @DeleteMapping("/doc/{documentId}")
    public void delete(@PathVariable Long documentId) { plasService.deleteDoc(documentId); }

    // 전자결재 문서 상신
    @PostMapping("/doc/{documentId}")
    public void send(@PathVariable Long documentId) { plasService.sendDoc(documentId); }

    // 전자결재 문서 승인
    @PostMapping("/doc/{documentId}/{employeeId}")
    public void approve(@PathVariable Long documentId, @PathVariable String employeeId) {
        plasService.approveDoc(documentId, employeeId);
    }

    // 내 문서 조회
    @GetMapping("/list/myDoc/{employeeId}")
    public List<DocListDto> myDocList(@PathVariable String employeeId) {
        return plasService.getMyDocList(employeeId);
    }

    // 내 부서 문서 조회
    @GetMapping("/list/myDeptDoc/{deptCode}")
    public List<DocListDto> myDeptDocList(@PathVariable String deptCode) {
        return plasService.getMyDeptDocList(deptCode);
    }

    // 내 결제 대기 문서 조회
    @GetMapping("/list/myAppvDoc/{employeeId}")
    public List<DocListDto> myAppvDocList(@PathVariable String employeeId) {
        return plasService.getMyAppvOrCCDocList("APPV", employeeId);
    }

    // 내 참조 문서 조회
    @GetMapping("/list/myCCDoc/{employeeId}")
    public List<DocListDto> myCCDocist(@PathVariable String employeeId) {
        return plasService.getMyAppvOrCCDocList("CC", employeeId);
    }

    // 전자결재 문서 상세 조회
    @GetMapping("/{documentId}")
    public DocumentResponseDto getDocument(@PathVariable Long documentId) {
        return plasService.getDocument(documentId);
    }

}
