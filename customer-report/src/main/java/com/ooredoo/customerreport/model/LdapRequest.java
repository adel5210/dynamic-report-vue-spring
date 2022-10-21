package com.ooredoo.customerreport.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author aalbediwy
 */
@Data
@Builder
public class LdapRequest {

    private String username;
    private String password;

}
