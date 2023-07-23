package com.erp.webtoon.service;

import com.erp.webtoon.domain.*;
import com.erp.webtoon.dto.plas.AppvLineListDto;
import com.erp.webtoon.dto.plas.DocListDto;
import com.erp.webtoon.dto.plas.DocTplListDto;
import com.erp.webtoon.repository.DocumentRcvRepository;
import com.erp.webtoon.repository.DocumentRepository;
import com.erp.webtoon.repository.DocumentTplRepository;
import com.erp.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlasService {

    private final DocumentRepository documentRepository;
    private final DocumentTplRepository documentTplRepository;
    private final DocumentRcvRepository documentRcvRepository;
    private final UserRepository userRepository;

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

    /*
        결재자 / 참조자 조회
    */
    @Transactional(readOnly = true)
    public List<AppvLineListDto> findAppvLineList() {
        List<User> appvLineList = userRepository.findAll();

        return appvLineList.stream()
                .map(user -> AppvLineListDto.builder()
                        .name(user.getName())
                        .deptName(user.getDeptName())
                        .teamNum(user.getTeamNum())
                        .position(user.getPosition())
                        .build())
                .collect(Collectors.toList());
    }

    /*
        내 문서 조회
    */
    @Transactional(readOnly = true)
    public List<DocListDto> findMyDocList(String employeeId) {
        User writeUser = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("문서 작성 직원의 정보가 존재하지 않습니다."));

        List<Document> myDocList = documentRepository.findAllByWriteUser(writeUser);

        return docStreamToList(myDocList);

    }

    /*
        내 부서 문서 조회
    */
    @Transactional(readOnly = true)
    public List<DocListDto> findMyDeptDocList(String deptCode) {

        List<User> myDeptUserList = userRepository.findAllByDeptCode(deptCode);

        List<Document> myDeptDocList = documentRepository.findAllByWriteUserIn(myDeptUserList);

        return docStreamToList(myDeptDocList);
    }

    /*
        내 결재대기 & 참조 문서 조회
    */
    @Transactional(readOnly = true)
    public List<DocListDto> findMyAppvOrCCDocList(String rcvType, String employeeId) {

        User appvUser = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보가 존재하지 않습니다."));

        List<DocumentRcv> myDocumentRcvList = documentRcvRepository.findAllByUserAndReceiveTypeAndStat(appvUser, rcvType, true);

        List<Document> myAppvDocList = myDocumentRcvList.stream()
                                            .map(DocumentRcv::getDocument).collect(Collectors.toList());

        return docStreamToList(myAppvDocList);
    }

    public List<DocListDto> docStreamToList(List<Document> documentList) {
        return documentList.stream()
                .map(doc -> DocListDto.builder()
                        .templateName(doc.getDocumentTpl().getTemplateName())
                        .title(doc.getTitle())
                        .stat(doc.getStat())
                        .writeUsername(doc.getWriteUser().getUsername())
                        .documentRcvNames(doc.getRcvNames())
                        .build())
                .collect(Collectors.toList());
    }

}
