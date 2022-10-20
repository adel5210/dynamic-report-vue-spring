package com.ooredoo.customerreport.controller;

import com.ooredoo.customerreport.dto.GenerateReportRequestDto;
import com.ooredoo.customerreport.dto.ReportParametersDto;
import com.ooredoo.customerreport.dto.ReportsDto;
import com.ooredoo.customerreport.model.JasperClassVariantTypes;
import com.ooredoo.customerreport.model.ReportBodyContent;
import com.ooredoo.customerreport.model.ReportServiceType;
import com.ooredoo.customerreport.model.RegisteredReportEnum;
import com.ooredoo.customerreport.service.ReportServiceFactory;
import com.ooredoo.customerreport.utils.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author aalbediwy
 */
@CrossOrigin(origins = "http://localhost:8186")
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Slf4j
public class CustomerReportController {

    private final ReportServiceFactory reportServiceFactory;

    @GetMapping("/lists")
    public ResponseEntity<List<ReportsDto>> getReportLists(){
        final List<ReportsDto> availableReports = Arrays.stream(RegisteredReportEnum.values())
                .map(m->ReportsDto.builder()
                            .index(m.getIndex())
                            .reportType(m.getReportLabelName())
                            .build())
                .collect(Collectors.toList());

        return new ResponseEntity<>(availableReports, HttpStatus.OK);
    }

    @PostMapping("/params/all")
    public ResponseEntity<List<ReportParametersDto>> getReportParameters(@RequestBody ReportsDto reportsDto){
        this.reportServiceFactory.setReportServiceType(ReportServiceType.JASPER);
        final Map<String, ?> parameters = reportServiceFactory.getParameters(RegisteredReportEnum.fromLabelName(reportsDto.getReportType()));
        final AtomicInteger position = new AtomicInteger(0);
        return ResponseEntity.ok(parameters.entrySet().stream()
                                    .map(m->ReportParametersDto.builder()
                                            .position(position.getAndIncrement())
                                            .parameterName(m.getKey())
                                            .defaultValue(((JasperClassVariantTypes) m.getValue()).getDefaultValue())
                                            .parameterLabel(StrUtil.toWords(m.getKey()))
                                            .parameterResult(((JasperClassVariantTypes) m.getValue()).getDefaultValue())
                                            .build())
                                    .collect(Collectors.toList()));
    }

    @PostMapping("/params/ui")
    public ResponseEntity<List<ReportParametersDto>> getReportParametersUi(@RequestBody ReportsDto reportsDto){
        return ResponseEntity.ok(Objects.requireNonNull(getReportParameters(reportsDto).getBody())
                .stream()
                .filter(f-> !f.getParameterName().startsWith("_"))
                .collect(Collectors.toList()));
    }

    @PostMapping("/generate")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getFile(@RequestBody GenerateReportRequestDto request) {
        this.reportServiceFactory.setReportServiceType(ReportServiceType.JASPER);
        final ReportBodyContent reportBodyContent = this.reportServiceFactory.getReportPDFFile(request.getReportsDto(), request.getCustomerIds());
        return ResponseEntity.ok()
                .header(reportBodyContent.getHeader().getFirst(), reportBodyContent.getHeader().getSecond())
                .contentType(reportBodyContent.getContentType())
                .contentLength(reportBodyContent.getContentLength())
                .body(reportBodyContent.getBody());
    }

}
