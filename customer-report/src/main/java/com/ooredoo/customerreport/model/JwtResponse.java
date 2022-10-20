package com.ooredoo.customerreport.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = 1150462084455085115L;

    private String accessToken;
//    private String refreshToken;
    private String username;

//    @Builder.Default
//    private final String tokenType = "Bearer";
}
