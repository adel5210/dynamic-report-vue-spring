package com.ooredoo.customerreport.service;

import com.ooredoo.customerreport.dto.ReportsDto;
import com.ooredoo.customerreport.model.RegisteredReportEnum;
import com.ooredoo.customerreport.model.ReportBodyContent;
import com.ooredoo.customerreport.model.ReportServiceType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author aalbediwy
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ReportServiceFactory {

    private final List<ReportService> processors;

    @Setter
    @Getter
    private ReportServiceType reportServiceType;

    @PostConstruct
    public void initialize(){
        processors.forEach(ReportService::initialize);
    }

    public Map<String, ?> getParameters(final RegisteredReportEnum report){
        return processors.stream()
                .filter(f->f.canProcess(this.reportServiceType))
                .findFirst()
                .map(m->m.getParameters(report))
                .orElse(new HashMap<>());
    }

    public ReportBodyContent getReportPDFFile(final ReportsDto reportsDto, List<String> customerIds) {

        final Supplier<IllegalArgumentException> exception = () ->
                new IllegalArgumentException("Fail to process report " + reportsDto.getReportType());

        return processors.stream()
                .filter(f->f.canProcess(this.reportServiceType))
                .findFirst()
                .map(m-> {
                    final File file = m.getReportPdf(reportsDto, customerIds);
                    InputStreamResource resource = null;
                    try {
                        resource = new InputStreamResource(new FileInputStream(file));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(null == resource){
                        throw exception.get();
                    }
                    return ReportBodyContent.builder()
                            .header(Pair.of(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+file.getName()))
                            .contentType(MediaType.parseMediaType(MediaType.APPLICATION_PDF_VALUE))
                            .contentLength(file.length())
                            .body(resource)
                            .build();

                })
                .orElseThrow(exception);

    }


}
