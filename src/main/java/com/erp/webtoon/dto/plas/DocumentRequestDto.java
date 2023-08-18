package com.erp.webtoon.dto.plas;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DocumentRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Builder.Default
    private char stat = 'N'; // 임시 저장 상태

    @CreationTimestamp
    private LocalDateTime regDate; // 작성일시

    private List<MultipartFile> uploadFiles;

    @NotNull
    private Long documentTpId;

    @NotBlank
    private String writeEmployeeId; // 작성자ID

    private List<DocumentRcvRequestDto> documentRcvRequests;

    private List<DocumentDataRequestDto> documentDataRequests;
}
