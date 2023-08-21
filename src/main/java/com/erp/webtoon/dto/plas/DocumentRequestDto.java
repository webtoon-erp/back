package com.erp.webtoon.dto.plas;
import com.erp.webtoon.domain.Document;
import com.erp.webtoon.domain.User;
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

    @CreationTimestamp
    private LocalDateTime regDate; // 작성일시

    private List<MultipartFile> uploadFiles;

    @NotNull
    private String templateName;

    @NotBlank
    private String writeEmployeeId; // 작성자ID

    private List<DocumentRcvRequestDto> documentRcvRequests;

    private List<DocumentDataDto> documentDataRequests;

    public Document toEntity(User writeUser) {
        return Document.builder()
                .title(title)
                .content(content)
                .stat('N')
                .regDate(regDate)
                .templateName(templateName)
                .writeUser(writeUser)
                .build();
    }
}
