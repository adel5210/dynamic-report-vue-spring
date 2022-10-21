package com.ooredoo.customerreport.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author aalbediwy
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "ldap")
public class LdapConfig {

    private String url;
    private String domainName;

}
