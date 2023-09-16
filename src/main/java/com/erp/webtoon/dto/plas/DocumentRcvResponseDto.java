package com.erp.webtoon.dto.plas;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DocumentRcvResponseDto {

    private int sortSequence;
    private String receiveType;
    private String userPosition;
    private String userName;
    
}
