package com.ooredoo.customerreport.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @author aalbediwy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportBodyContent {

    private Pair<String, String> header;
    private MediaType contentType;
    private Long contentLength;
    private InputStreamResource body;

}
