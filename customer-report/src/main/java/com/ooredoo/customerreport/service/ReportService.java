package com.ooredoo.customerreport.service;

import com.ooredoo.customerreport.dto.ReportsDto;
import com.ooredoo.customerreport.model.ReportServiceType;
import com.ooredoo.customerreport.model.RegisteredReportEnum;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author aalbediwy
 */
public interface ReportService {

    boolean canProcess(ReportServiceType types);

    void initialize();

    void exportPDF(String fileName, JasperPrint jasperPrint);

    // name - type
    Map<String, ?> getParameters(RegisteredReportEnum registeredReportEnum);

    File getReportPdf(ReportsDto reportsDto, List<String> customerIds);

}
