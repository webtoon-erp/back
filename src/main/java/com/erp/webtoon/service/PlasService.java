package com.erp.webtoon.service;

import com.erp.webtoon.domain.*;
import com.erp.webtoon.dto.message.MessageSaveDto;
import com.erp.webtoon.dto.plas.*;
import com.erp.webtoon.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlasService {

    private final DocumentRepository documentRepository;
    private final DocumentTplRepository documentTplRepository;
    private final DocumentRcvRepository documentRcvRepository;
    private final DocumentDataRepository documentDataRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final MessageService messageService;

    /*
        템플릿 조회
    */
    @Transactional(readOnly = true)
    public List<DocTplListDto> getTemplateList() {
        List<DocumentTpl> templateList = documentTplRepository.findAll();

        return templateList.stream()
                .map(template -> DocTplListDto.builder()
                        .id(template.getId())
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
    public List<AppvLineListDto> getAppvLineList() {
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
    public List<DocListDto> getMyDocList(String employeeId) {
        User writeUser = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("문서 작성 직원의 정보가 존재하지 않습니다."));

        List<Document> myDocList = documentRepository.findAllByWriteUser(writeUser);

        return docStreamToList(myDocList);

    }

    /*
        내 부서 문서 조회
    */
    @Transactional(readOnly = true)
    public List<DocListDto> getMyDeptDocList(String deptCode) {

        List<User> myDeptUserList = userRepository.findAllByDeptCode(deptCode);

        List<Document> myDeptDocList = documentRepository.findAllByWriteUserIn(myDeptUserList);

        return docStreamToList(myDeptDocList);
    }

    /*
        내 결재대기 & 참조 문서 조회
    */
    @Transactional(readOnly = true)
    public List<DocListDto> getMyAppvOrCCDocList(String rcvType, String employeeId) {

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
                        .reg_date(doc.getRegDate())
                        .writeDeptName(doc.getWriteUser().getDeptName())
                        .writeUsername(doc.getWriteUser().getUsername())
                        .documentRcvNames(doc.getRcvNames())
                        .stat(doc.getStat())
                        .build())
                .collect(Collectors.toList());
    }

    /*
        전자결재 문서 저장
     */
    public void addDoc(DocumentRequestDto dto) throws IOException {
        DocumentTpl documentTpl = documentTplRepository.findById(dto.getDocumentTpId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 문서 템플릿 정보입니다."));

        User writeUser = userRepository.findByEmployeeId(dto.getWriteEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 작성자 정보입니다."));

        Document document = dto.toEntity(documentTpl, writeUser);

        // 문서 DATA 저장
        if (!dto.getDocumentDataRequests().isEmpty()) {
            List<DocumentData> documentDataList = dto.getDocumentDataRequests().stream()
                    .map(dataRequestDto -> dataRequestDto.toEntity(document))
                    .collect(Collectors.toList());
            documentDataRepository.saveAll(documentDataList);
        }

        // 문서 수신자 저장
        if (!dto.getDocumentRcvRequests().isEmpty()) {
            List<DocumentRcv> documentRcvList = dto.getDocumentRcvRequests().stream()
                    .map(rcvRequestDto -> {
                        User rcvUser = userRepository.findByEmployeeId(rcvRequestDto.getRcvEmployeeId())
                                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 결재자/참조자 정보입니다."));

                        return rcvRequestDto.toEntity(document, rcvUser);
                    })
                    .collect(Collectors.toList());
            documentRcvRepository.saveAll(documentRcvList);
        }

        // 파일 저장
        if (!dto.getUploadFiles().isEmpty()) {
            for (MultipartFile file: dto.getUploadFiles()) {
                File saveFile = fileService.save(file);
                saveFile.updateFileDocument(document);
                document.getFiles().add(saveFile);
            }
        }

        // 문서 저장
        documentRepository.save(document);
    }

    /*
        전자결재 문서 상신
     */
    public void sendDoc(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 문서 입니다."));

        document.changeStat('Y');

        // 1번 결재자 조회
        DocumentRcv documentRcv = documentRcvRepository.findByDocumentAndReceiveTypeAndSortSequence(document, "APPV", 1)
                .orElseThrow(() -> new EntityNotFoundException("해당 문서에 결재자가 존재하지 않습니다."));

        documentRcv.changeStat('Y');

        MessageSaveDto messageSaveDto = MessageSaveDto.builder()
                .msgType("dm")
                .content("새 전자결재 문서가 상신되었습니다. 문서명 - " + document.getTitle())
                .refId(documentId)
                .programId("plas")
                .build();

        Message message = messageSaveDto.toEntity(documentRcv.getUser(), document.getWriteUser());
        messageService.addMsg(message);
    }

    /*
       전자결재 문서 상세 조회
     */
    public DocumentResponseDto getDocument(Long documentId) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 문서 정보입니다."));

        return DocumentResponseDto.builder()
                .title(document.getTitle())
                .content(document.getContent())
                .regDate(document.getRegDate())
                .templateName(document.getDocumentTpl().getTemplateName())
                .writeUserDeptName(document.getWriteUser().getDeptName())
                .writeUserTeamNum(document.getWriteUser().getTeamNum())
                .writeUserPosition(document.getWriteUser().getPosition())
                .writeUserEmployeeId(document.getWriteUser().getEmployeeId())
                .writeUserName(document.getWriteUser().getUsername())
                .uploadFiles(document.getFileResponse())
                .documentRcvResponses(document.getRcvResponse())
                .documentDataResponses(document.getDataResponse())
                .build();

    }
}
