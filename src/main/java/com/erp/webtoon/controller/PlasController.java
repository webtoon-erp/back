package com.erp.webtoon.controller;

import com.erp.webtoon.dto.plas.AppvLineListDto;
import com.erp.webtoon.dto.plas.DocListDto;
import com.erp.webtoon.dto.plas.DocTplListDto;
import com.erp.webtoon.service.PlasService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlasController {

    private final PlasService plasService;

    // 템플릿 리스트 조회
    @GetMapping("/addDoc/templateList")
    public List<DocTplListDto> templateList() {
        return plasService.findTemplateList();
    }

    // 결재자 / 참조자 조회 기능
    @GetMapping("/addDoc/appvLineList")
    public List<AppvLineListDto> appvLineList() {
        return plasService.findAppvLineList();
    }

    // 내 문서 조회
    @GetMapping("/list/myDoc/{employeeId}")
    public List<DocListDto> myDocList(@PathVariable("employeeId") String employeeId) {
        return plasService.findMyDocList(employeeId);
    }

    // 내 문서 조회
    @GetMapping("/list/myDeptDoc/{deptCode}")
    public List<DocListDto> myDeptDocList(@PathVariable("deptCode") String deptCode) {
        return plasService.findMyDeptDocList(deptCode);
    }

}
