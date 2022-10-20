package com.ooredoo.customerreport.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author aalbediwy
 */
@Getter
@AllArgsConstructor
public enum RegisteredReportEnum {

    SOA(1,"statement_of_account.jrxml", "Statement of account", ReportServiceType.JASPER,"_customerIds"),
    CERTIFICATE(2,"print_certificate.jrxml", "Print Certificate", ReportServiceType.JASPER,"_customerIds"),
    ;

    private final Integer index;
    private final String reportConfigFileName;
    private final String reportLabelName;
    private final ReportServiceType reportServiceType;
    private final String customerIdsParameterName;

    public static RegisteredReportEnum fromLabelName(String name){
        return Arrays.stream(values()).filter(f->f.reportLabelName.equals(name))
                .findFirst().orElse(null);
    }

}
