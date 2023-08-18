package com.erp.webtoon.domain;

import com.erp.webtoon.dto.plas.DocumentFileResponseDto;
import com.erp.webtoon.dto.plas.DocumentDataDto;
import com.erp.webtoon.dto.plas.DocumentRcvResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private Long id;

    private String title;

    private String content;

    private char stat;

    private LocalDateTime regDate; // 작성일시

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();   // 참조된 파일들

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_tpl_id")
    private DocumentTpl documentTpl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "user_id" , name = "write_user_id")
    private User writeUser;   // 작성자

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<DocumentRcv> documentRcvs = new ArrayList<>();   // 수신자 목록

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<DocumentData> documentDataList = new ArrayList<>();   // 데이터 목록

    @Builder
    public Document(String title, String content, char stat, LocalDateTime regDate, DocumentTpl documentTpl, User writeUser) {
        this.title = title;
        this.content = content;
        this.stat = stat;
        this.regDate = regDate;
        this.documentTpl = documentTpl;
        this.writeUser = writeUser;
    }


    //수신자 목록의 이름 불러오기
    public List<String> getRcvNames() {
        return documentRcvs.stream()
                .map(documentRcv -> documentRcv.getUser().getName())
                .collect(Collectors.toList());
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
