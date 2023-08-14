package com.erp.webtoon.dto.attendece;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthlyOvertimeSummaryDto {

    private String janOvertime;
    private String febOvertime;
    private String marOvertime;
    private String aprOvertime;
    private String mayOvertime;
    private String junOvertime;
    private String julOvertime;
    private String augOvertime;
    private String sepOvertime;
    private String octOvertime;
    private String novOvertime;
    private String devOvertime;

}
