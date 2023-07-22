package com.erp.webtoon.controller;

import com.erp.webtoon.service.FileService;
import com.erp.webtoon.service.WebtoonDtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebtoonDtController {

    private final WebtoonDtService webtoonDtService;

    private final FileService fileService;


}
