package com.erp.webtoon.service;

import com.erp.webtoon.domain.DocumentTpl;
import com.erp.webtoon.dto.plas.DocTplListDto;
import com.erp.webtoon.repository.DocumentTplRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlasService {

    private final DocumentTplRepository documentTplRepository;

    /*
        템플릿 조회
    */
    @Transactional(readOnly = true)
    public List<DocTplListDto> findTemplateList() {
        List<DocumentTpl> templateList = documentTplRepository.findAll();

        return templateList.stream()
                .map(template -> DocTplListDto.builder()
                        .templateName(template.getTemplateName())
                        .intro(template.getIntro())
                        .template(template.getTemplate())
                        .build())
                .collect(Collectors.toList());
    }

}
