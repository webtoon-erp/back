package com.erp.webtoon.controller;

import com.erp.webtoon.dto.user.UserListResponseDto;
import com.erp.webtoon.service.HrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/hr")
public class HrController {

    private HrService hrService;

    @GetMapping("/list")
    public List<UserListResponseDto> search(){
        return hrService.getAllUsers();
    }
}
