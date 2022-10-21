package com.ooredoo.customerreport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author aalbediwy
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportParametersDto {

    private String parameterName;
    private Object defaultValue;
    private Integer position;
    private Object parameterResult;
    private String parameterLabel;

}
