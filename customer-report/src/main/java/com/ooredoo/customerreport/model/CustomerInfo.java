package com.ooredoo.customerreport.model;

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
public class CustomerInfo {

    private String id;
    private String customerId;
    private String name;
    private String civilId;
    private String msisdn;

}
