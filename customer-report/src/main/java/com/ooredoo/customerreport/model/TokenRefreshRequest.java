package com.ooredoo.customerreport.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author aalbediwy
 */
@Data
@Builder
public class TokenRefreshRequest {

    private String refreshToken;

}
