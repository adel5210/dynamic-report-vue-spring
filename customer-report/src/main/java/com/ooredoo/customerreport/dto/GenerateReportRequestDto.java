package com.ooredoo.customerreport.dto;

import com.ooredoo.customerreport.dto.ReportsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author aalbediwy
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GenerateReportRequestDto {

    private List<String> customerIds;
    private ReportsDto reportsDto;

}
