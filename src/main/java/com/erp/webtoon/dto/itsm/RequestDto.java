package com.erp.webtoon.dto.itsm;

import com.erp.webtoon.domain.File;
import com.erp.webtoon.domain.Request;
import com.erp.webtoon.domain.RequestDt;
import com.erp.webtoon.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private String reqType;

    private String title;

    private String content;

    private int step;

    private LocalDate dueDate;     // 기한 일자

    private LocalDate doneDate;    // 완료 일자

    private String reqUserId;   // 요청자 사번

    private String itUserId;    // 담당자 사번

    private List<MultipartFile> files;   // 요청에 쓰이는 첨부파일들

    private List<RequestDt> requestDts;
}
