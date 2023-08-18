package com.erp.webtoon.controller;

import com.erp.webtoon.dto.plas.AppvLineListDto;
import com.erp.webtoon.dto.plas.DocListDto;
import com.erp.webtoon.dto.plas.DocTplListDto;
import com.erp.webtoon.dto.plas.DocumentRequestDto;
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

    // 템플릿 리스트 조회
    @GetMapping("/addDoc/templateList")
    public List<DocTplListDto> templateList() {
        return plasService.getTemplateList();
    }

    // 결재자 / 참조자 조회 기능
    @GetMapping("/addDoc/appvLineList")
    public List<AppvLineListDto> appvLineList() {
        return plasService.getAppvLineList();
    }

    // 전자결재 문서 저장
    @PostMapping("/addDoc/save")
    public void save(@RequestBody DocumentRequestDto dto) throws IOException { plasService.addDoc(dto); }

    // 내 문서 조회
    @GetMapping("/list/myDoc/{employeeId}")
    public List<DocListDto> myDocList(@PathVariable("employeeId") String employeeId) {
        return plasService.getMyDocList(employeeId);
    }

    // 내 부서 문서 조회
    @GetMapping("/list/myDeptDoc/{deptCode}")
    public List<DocListDto> myDeptDocList(@PathVariable("deptCode") String deptCode) {
        return plasService.getMyDeptDocList(deptCode);
    }

    // 내 결제 대기 문서 조회
    @GetMapping("/list/myAppvDoc/{employeeId}")
    public List<DocListDto> myAppvDocList(@PathVariable("employeeId") String employeeId) {
        return plasService.getMyAppvOrCCDocList("APPV", employeeId);
    }

    // 내 참조 문서 조회
    @GetMapping("/list/myCCDoc/{employeeId}")
    public List<DocListDto> myCCDocist(@PathVariable("employeeId") String employeeId) {
        return plasService.getMyAppvOrCCDocList("CC", employeeId);
    }

}
