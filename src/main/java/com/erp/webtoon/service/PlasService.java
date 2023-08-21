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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlasService {

    private final DocumentRepository documentRepository;
    private final DocumentRcvRepository documentRcvRepository;
    private final DocumentDataRepository documentDataRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final MessageService messageService;

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

        List<Document> myDeptDocList = documentRepository.findAllByWriteUserInAndStatNot(myDeptUserList, 'N');

        return docStreamToList(myDeptDocList);
    }

    /*
        내 결재대기 & 참조 문서 조회
    */
    @Transactional(readOnly = true)
    public List<DocListDto> getMyAppvOrCCDocList(String rcvType, String employeeId) {

        User appvUser = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보가 존재하지 않습니다."));

        List<DocumentRcv> myDocumentRcvList = documentRcvRepository.findAllByUserAndReceiveTypeAndDocument_StatNot(appvUser, rcvType, 'N');

        List<Document> myAppvDocList = myDocumentRcvList.stream()
                .map(DocumentRcv::getDocument).collect(Collectors.toList());

        return docStreamToList(myAppvDocList);
    }

    public List<DocListDto> docStreamToList(List<Document> documentList) {
        return documentList.stream()
                .map(doc -> DocListDto.builder()
                        .templateName(doc.getTemplateName())
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

        User writeUser = userRepository.findByEmployeeId(dto.getWriteEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 작성자 정보입니다."));

        Document document = dto.toEntity(writeUser);

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
            for (MultipartFile file : dto.getUploadFiles()) {
                File saveFile = fileService.save(file);
                saveFile.updateFileDocument(document);
                document.getFiles().add(saveFile);
            }
        }

        // 문서 저장
        documentRepository.save(document);
    }

    /*
        연차 사용 신청 등록
     */
    public void addDayOffDoc(DayOffDocumentRequestDto dto) {

        User writeUser = userRepository.findByEmployeeId(dto.getWriteEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 작성자 정보입니다."));

        // 문서 저장
        Document document = dto.toDocumentEntity(writeUser);
        documentRepository.save(document);

        // 문서 Data 저장
        DocumentData documentData = dto.toDocDataEntity(document);
        documentDataRepository.save(documentData);

        // 문서 결재선 저장 - 해당 부서 & 팀 최고 결재자
        User appvUser = getAppvUser(writeUser);
        DocumentRcv documentRcv = dto.toDocRcvEntity(document, appvUser);
        documentRcvRepository.save(documentRcv);

        String content = "새 전자결재 문서가 상신되었습니다. 문서명 - " + document.getTitle();
        sendMsg(document.getId(), appvUser, writeUser, content);

    }

    /*
        전자결재 문서 삭제
     */
    public void deleteDoc(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 문서 입니다."));

        if (document.getStat() == 'Y') documentRepository.delete(document);
        else throw new RuntimeException("이미 상신이나 완료된 문서는 삭제할 수 없습니다.");
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

        String content = "새 전자결재 문서가 상신되었습니다. 문서명 - " + document.getTitle();
        sendMsg(documentId, document.getWriteUser(), documentRcv.getUser(), content);
    }

    /*
        전자결재 문서 승인
     */
    public void approveDoc(Long documentId, String employeeId) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 문서 정보입니다."));

        User user = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자 정보입니다."));

        // 참조자 제외, 결제자만 조회
        List<DocumentRcv> approvers = document.getApprovers();

        boolean approved = false;

        for (int i = 0; i < approvers.size(); i++) {
            DocumentRcv documentRcv = approvers.get(i);

            if (documentRcv.getUser() == user && documentRcv.getStat() == 'Y') {
                documentRcv.changeStat('C'); // 완료 상태로 변경

                if (i != approvers.size() - 1) { // 현재 결재자가 최종 결재자가 아닐 경우 다음 결재자 업데이트
                    DocumentRcv nextDocumentRcv = approvers.get(i + 1);
                    nextDocumentRcv.changeStat('Y');

                    // 다음 결재자 알림
                    String content = "새 전자결재 문서가 상신되었습니다. 문서명 - " + document.getTitle();
                    sendMsg(documentId, nextDocumentRcv.getUser(), document.getWriteUser(), content);
                } else { // 현재 결재자가 최종 결재자일 경우 문서 업데이트
                    document.changeStat('C'); // 완료 상태로 변경

                    // 문서 완료 시 문서 작성자 알림
                    String content = "전자결재 문서가 최종 승인 및 완료되었습니다. 문서명 - " + document.getTitle();
                    sendMsg(documentId, document.getWriteUser(), documentRcv.getUser(), content);
                }

                approved = true;
                break;
            }
        }

        if (!approved) {
            throw new RuntimeException("해당 사용자의 승인 권한 아직 없거나 이미 처리되었습니다.");
        }

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
                .templateName(document.getTemplateName())
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

    private void sendMsg(Long documentId, User rcvUser, User sendUser, String content) {
        MessageSaveDto messageSaveDto = MessageSaveDto.builder()
                .channel("dm")
                .content(content)
                .refId(documentId)
                .programId("plas")
                .build();

        Message message = messageSaveDto.toEntity(rcvUser, sendUser);
        messageService.addMsg(message);
    }

    private User getAppvUser(User writeUser) {

        List<User> sameDeptUsers = userRepository.findByDeptCodeAndTeamNum(writeUser.getDeptCode(), writeUser.getTeamNum());

        List<String> userOrders = List.of("사원", "대리", "차장", "과장", "부장");

        User appvUser = sameDeptUsers.stream()
                .max(Comparator.comparingInt(user -> userOrders.indexOf(user.getPosition())))
                .orElseThrow(() -> new NoSuchElementException("해당 직원이 속한 팀의 최고 결재선 정보가 존재하지 않습니다."));

        return appvUser;
    }
}
