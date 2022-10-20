package com.ooredoo.customerreport.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = 1150462084455085115L;

    private final String accessToken;
    private final String username;
}
