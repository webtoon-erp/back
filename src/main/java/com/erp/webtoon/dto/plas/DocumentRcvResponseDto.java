package com.erp.webtoon.dto.plas;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentRcvResponseDto {

    private int sortSequence;
    private String receiveType;
    private String userPosition;
    private String userName;
    
}
