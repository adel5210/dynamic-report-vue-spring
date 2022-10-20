package com.ooredoo.customerreport.config;

import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author aalbediwy
 */
@Configuration
public class JasperReportConfig {

    @Value("${report.default.metadataAuthor}")
    private String metadataAuthor;

    @Bean
    public SimplePdfReportConfiguration simplePdfReportConfiguration(){
        final SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);
        return reportConfig;
    }

    @Bean
    public SimplePdfExporterConfiguration simplePdfExporterConfiguration(){
        final SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor(metadataAuthor);
        exportConfig.setAllowedPermissionsHint("PRINTING");
        return exportConfig;
    }

}
