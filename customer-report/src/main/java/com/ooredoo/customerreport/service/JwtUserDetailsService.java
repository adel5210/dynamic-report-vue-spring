package com.ooredoo.customerreport.service;

import com.ooredoo.customerreport.model.User;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService {

    public User loadUserByUsername(String username) {
        return User.builder()
                .username(username)
                .build();
    }
}
