package com.ooredoo.customerreport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author aalbediwy
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSearchDto {

    private String customerIds;

}
