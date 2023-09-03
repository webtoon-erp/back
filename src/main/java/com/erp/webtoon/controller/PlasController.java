package com.erp.webtoon.controller;

import com.erp.webtoon.dto.common.ErrorResponseDto;
import com.erp.webtoon.dto.plas.ApproverListDto;
import com.erp.webtoon.dto.plas.DayOffDocumentRequestDto;
import com.erp.webtoon.dto.plas.DocListDto;
import com.erp.webtoon.dto.plas.DocumentRequestDto;
import com.erp.webtoon.dto.plas.DocumentResponseDto;
import com.erp.webtoon.service.PlasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plas")
public class PlasController {

    private final PlasService plasService;

    /**
     * 결재자 / 참조자 조회 기능
     */
    @GetMapping("/approvers")
    public List<ApproverListDto> getApprovers() {
        return plasService.getApproverList();
    }

    /**
     * 전자결재 문서 저장
     */
    @PostMapping("/documents")
    public void save(@RequestPart DocumentRequestDto dto, @RequestPart("files")List<MultipartFile> files) throws IOException {
        plasService.addDoc(dto, files);
    }

    /**
     * 연차 사용 신청 등록
     */
    @PostMapping("/documents/dayOff")
    public void registerDayOff(@RequestBody DayOffDocumentRequestDto dto) {
        plasService.addDayOffDoc(dto);
    }

    /**
     * 전자결재 문서 삭제
     */
    @DeleteMapping("/documents/{documentId}")
    public void delete(@PathVariable Long documentId) {
        plasService.deleteDoc(documentId);
    }

    /**
     * 전자결재 문서 상신
     */
    @PatchMapping("/documents/{documentId}")
    public void submit(@PathVariable Long documentId) {
        plasService.submitDoc(documentId);
    }

    /**
     * 전자결재 문서 승인
     */
    @PatchMapping("/documents/{documentId}/{employeeId}")
    public void approve(@PathVariable Long documentId, @PathVariable String employeeId) {
        plasService.approveDoc(documentId, employeeId);
    }

    /**
     * 내 문서 조회
     */
    @GetMapping("/documents/my/{employeeId}")
    public List<DocListDto> getMyDocuments(@PathVariable String employeeId) {
        return plasService.getMyDocList(employeeId);
    }

    /**
     * 내 부서 문서 조회
     */
    @GetMapping("/documents/myDept/{deptCode}")
    public List<DocListDto> getMyDeptDocuments(@PathVariable String deptCode) {
        return plasService.getMyDeptDocList(deptCode);
    }

    /**
     * 내 결재 문서 조회 -> 결재 완료 + 결재 대기 조회
     */
    @GetMapping("/documents/myAppv/{employeeId}")
    public List<DocListDto> getMyAppvDocuments(@PathVariable String employeeId) {
        return plasService.getMyAppvOrCCDocList("APPV", employeeId);
    }

    /**
     * 내 참조 문서 조회
     */
    @GetMapping("/documents/myCC/{employeeId}")
    public List<DocListDto> getMyCCDocuments(@PathVariable String employeeId) {
        return plasService.getMyAppvOrCCDocList("CC", employeeId);
    }

    /**
     * 전자결재 문서 상세 조회
     */
    @GetMapping("/documents/{documentId}")
    public DocumentResponseDto getDocumentDetails(@PathVariable Long documentId) {
        return plasService.getDocument(documentId);
    }
}
