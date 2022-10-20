package com.ooredoo.customerreport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author aalbediwy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportsDto {

    private Integer index;
    private String reportType;

    private List<ReportParametersDto> reportParameters;
}
