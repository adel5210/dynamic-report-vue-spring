package com.ooredoo.customerreport.controller;

import com.ooredoo.customerreport.service.JwtTokenUtil;
import com.ooredoo.customerreport.model.JwtRequest;
import com.ooredoo.customerreport.model.JwtResponse;
import com.ooredoo.customerreport.model.User;
import com.ooredoo.customerreport.service.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:8186")
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        final String username = authenticationRequest.getUsername();
        final String password = authenticationRequest.getPassword();

        authenticate(username, password);
        final User userDetails = jwtUserDetailsService.loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(JwtResponse.builder()
                                    .accessToken(token)
                                    .username(username)
                                    .build());

    }

    private void authenticate(String username, String password) throws Exception {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username,password));
        }catch (DisabledException e){
            throw new Exception("USER_DISABLED", e);
        }catch (BadCredentialsException e){
            throw new Exception("INVALID_CREDENTIALS",e);
        }
    }
}
