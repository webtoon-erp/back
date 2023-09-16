package com.erp.webtoon.domain;

import com.erp.webtoon.dto.plas.DocumentFileResponseDto;
import com.erp.webtoon.dto.plas.DocumentDataDto;
import com.erp.webtoon.dto.plas.DocumentRcvResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private Long id;

    private String title;

    private String content;

    private char stat; // N : 임시 , Y : 상신 , C : 완료

    @CreatedDate
    private LocalDateTime regDate; // 작성일시

    private String templateName; // 템플릿 이름

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();   // 참조된 파일들

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "user_id" , name = "write_user_id")
    private User writeUser;   // 작성자

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<DocumentRcv> documentRcvs = new ArrayList<>();   // 수신자 목록

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<DocumentData> documentDataList = new ArrayList<>();   // 데이터 목록

    @Builder
    public Document(String title, String content, char stat, LocalDateTime regDate, String templateName, User writeUser) {
        this.title = title;
        this.content = content;
        this.stat = stat;
        this.regDate = regDate;
        this.templateName = templateName;
        this.writeUser = writeUser;
    }


    //수신자 목록의 이름 불러오기
//    public List<String> getRcvNames() {
//        return documentRcvs.stream()
//                .map(documentRcv -> documentRcv.getUser().getName())
//                .collect(Collectors.toList());
//    }

    // 결재대기자 찾기
    public String getCurrentApprover() {
        return documentRcvs.stream()
                .filter(documentRcv -> documentRcv.getReceiveType().equals("APPV") && documentRcv.getStat() == 'Y')
                .findFirst()
                .map(documentRcv -> documentRcv.getUser().getUsername())
                .orElse("");
    }

    // 최종결재자 찾기
    public String getLastApprover() {
        return documentRcvs.stream()
                .filter(documentRcv -> documentRcv.getReceiveType().equals("APPV"))
                .max(Comparator.comparingInt(DocumentRcv::getSortSequence))
                .map(documentRcv -> documentRcv.getUser().getUsername())
                .orElse("");
    }


    public void changeStat(char stat) { this.stat = stat; }

    public List<DocumentFileResponseDto> getFileResponse() {
        return files.stream()
                .map(file -> DocumentFileResponseDto.builder()
                        .id(file.getId())
                        .originName(file.getOriginName())
                        .build())
                .collect(Collectors.toList());
    }

    public List<DocumentRcvResponseDto> getRcvResponse() {
        return documentRcvs.stream()
                .map(documentRcv -> DocumentRcvResponseDto.builder()
                        .sortSequence(documentRcv.getSortSequence())
                        .receiveType(documentRcv.getReceiveType())
                        .userPosition(documentRcv.getUser().getPosition())
                        .userName(documentRcv.getUser().getName())
                        .build())
                .collect(Collectors.toList());
    }

    public List<DocumentDataDto> getDataResponse() {
        return documentDataList.stream()
                .map(documentData -> DocumentDataDto.builder()
                        .fromDate(documentData.getFromDate())
                        .toDate(documentData.getToDate())
                        .deptCode(documentData.getDeptCode())
                        .deptName(documentData.getDeptName())
                        .company(documentData.getCompany())
                        .expenseType(documentData.getExpenseType())
                        .count(documentData.getCount())
                        .price(documentData.getPrice())
                        .supAmt(documentData.getSupAmt())
                        .vatAmt(documentData.getVatAmt())
                        .totalAmt(documentData.getTotalAmt())
                        .remark(documentData.getRemark())
                        .build())
                .collect(Collectors.toList());
    }

    public List<DocumentRcv> getApprovers() {
        return documentRcvs.stream()
                .filter(documentRcv -> documentRcv.getReceiveType().equals("APPV"))
                .collect(Collectors.toList());
    }
}
