package com.erp.webtoon.dto.plas;

import com.erp.webtoon.domain.Document;
import com.erp.webtoon.domain.DocumentData;
import com.erp.webtoon.domain.DocumentRcv;
import com.erp.webtoon.domain.User;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class DayOffDocumentRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @CreationTimestamp
    private LocalDateTime regDate;      // 작성일시

    @NotBlank
    private String templateName;

    @NotBlank
    private String writeEmployeeId;     // 작성자ID

    @NotBlank
    private LocalDateTime dayOffDate;   // 연차일자

    public Document toDocumentEntity(User writeUser) {
        return Document.builder()
                .title(title)
                .content(content)
                .stat('Y')
                .regDate(regDate)
                .templateName(templateName)
                .writeUser(writeUser)
                .build();
    }

    public DocumentData toDocDataEntity(Document document) {
        return DocumentData.builder()
                .fromDate(dayOffDate)
                .document(document)
                .build();
    }

    public DocumentRcv toDocRcvEntity(Document document, User appvUser) {
        return DocumentRcv.builder()
                .sortSequence(1)
                .receiveType("APPV")
                .stat('Y')
                .document(document)
                .user(appvUser)
                .build();
    }
}
