package com.ooredoo.customerreport.service;

import com.ooredoo.customerreport.dto.ReportParametersDto;
import com.ooredoo.customerreport.dto.ReportsDto;
import com.ooredoo.customerreport.model.JasperClassVariantTypes;
import com.ooredoo.customerreport.model.RegisteredReportEnum;
import com.ooredoo.customerreport.model.ReportServiceType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRValidationException;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author aalbediwy
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class JasperReportService implements ReportService{

    private static final String PREFIX_RESOURCE_REPORT_PATH = System.getProperty("java.io.tmpdir")+"\\";
    public static final String SUB_DIR_REPORTS = "reports";
    public static final int MAX_COMPILE_REPORT_RETIRES = 5;

    private final DataSource dataSource;

    private final SimplePdfReportConfiguration simplePdfReportConfiguration;
    private final SimplePdfExporterConfiguration simplePdfExporterConfiguration;

    private static final Map<RegisteredReportEnum, JasperReport> compiledReports = new HashMap<>();

    @Override
    public boolean canProcess(ReportServiceType types) {
        return types.equals(ReportServiceType.JASPER);
    }

    @Override
    public void initialize() {
        processCompiledReports();
    }

    @Override
    public Map<String, ?> getParameters(RegisteredReportEnum registeredReportEnum) {
        compileReport(registeredReportEnum.getReportConfigFileName());
        final Map<String, JasperClassVariantTypes> parameters = new HashMap<>();
        Arrays.stream(compiledReports.get(registeredReportEnum).getParameters())
                .filter(f->!f.isSystemDefined())
                .forEach(e -> parameters.put(e.getName(), JasperClassVariantTypes.fromClassName(e.getValueClassName())));
        return parameters;
    }

    @SneakyThrows
    @Override
    public File getReportPdf(ReportsDto reportsDto, List<String> customerIds) {
        final RegisteredReportEnum reportType = RegisteredReportEnum.fromLabelName(reportsDto.getReportType());
        final Map<String, ?> params = getParameters(reportType);
        final Map<String, Object> parameters = new HashMap<>();
        for(ReportParametersDto reportParametersDto:  reportsDto.getReportParameters()){
            final JasperClassVariantTypes variantType = ((JasperClassVariantTypes) params.get(reportParametersDto.getParameterName()));
            switch(variantType){
                case BIG_DECIMAL: parameters.put(reportParametersDto.getParameterName(), new BigDecimal(reportParametersDto.getParameterResult().toString())); break;
                case BOOLEAN: parameters.put(reportParametersDto.getParameterName(), Boolean.parseBoolean(reportParametersDto.getParameterResult().toString())); break;
                case STRING: parameters.put(reportParametersDto.getParameterName(), reportParametersDto.getParameterResult().toString()); break;
                case DOUBLE: parameters.put(reportParametersDto.getParameterName(), Double.parseDouble(reportParametersDto.getParameterResult().toString())); break;
                case INTEGER: parameters.put(reportParametersDto.getParameterName(), Integer.parseInt(reportParametersDto.getParameterResult().toString())); break;
                case LONG: parameters.put(reportParametersDto.getParameterName(), Long.parseLong(reportParametersDto.getParameterResult().toString())); break;
            }
        }
        parameters.put(reportType.getCustomerIdsParameterName(), customerIds);
        final JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReports.get(reportType),
                                                    parameters, dataSource.getConnection());
        final String fileName = UUID.randomUUID() + ".pdf";
        exportPDF(fileName, jasperPrint);
        return new File(PREFIX_RESOURCE_REPORT_PATH + fileName);
    }

    @SneakyThrows
    private JasperReport compileReport(final String filePath){
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("\\"+SUB_DIR_REPORTS +"\\"+filePath);
        return JasperCompileManager.compileReport(inputStream);
    }


    @SneakyThrows
    @Override
    public void exportPDF(String fileName, JasperPrint jasperPrint) {
        final JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(PREFIX_RESOURCE_REPORT_PATH+fileName));
        exporter.setConfiguration(simplePdfReportConfiguration);
        exporter.setConfiguration(simplePdfExporterConfiguration);
        exporter.exportReport();
    }

    @SneakyThrows
    private void tempSaveReport(RegisteredReportEnum reportTypes, JasperReport jasperReport){
        JRSaver.saveObject(jasperReport, reportTypes.getReportConfigFileName());
    }

    private void processCompiledReports() {
        Arrays.stream(RegisteredReportEnum.values()).filter(f -> f.getReportServiceType().equals(ReportServiceType.JASPER))
            .forEach(e -> {
                try {
                    final JasperReport report = compileReport(e.getReportConfigFileName());
                    // tempSaveReport(e, report);
                    compiledReports.put(e, report);
                }catch (Exception ex){
                    log.error("Jasper report encounter exception upon compiling ("+e.getReportConfigFileName()+"): "+ex.getMessage());
                }
            });

    }

}
