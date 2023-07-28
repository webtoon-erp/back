package com.erp.webtoon.dto.user;

import com.erp.webtoon.domain.File;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class QualificaitonRequestDto {
    private String qlfcType;    // 자격증 타입
    private String content;     // 내용
    private LocalDate qlfcDate;    // 만료일자
    private List<File> files; // 첨부한 파일목록
}
