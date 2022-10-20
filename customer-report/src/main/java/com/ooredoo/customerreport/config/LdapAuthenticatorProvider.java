package com.ooredoo.customerreport.config;

import com.ooredoo.customerreport.config.LdapConfig;
import com.ooredoo.customerreport.model.LdapRequest;
import com.ooredoo.customerreport.model.LdapResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

/**
 * @author aalbediwy
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LdapAuthenticatorProvider implements AuthenticationProvider {

    private final LdapConfig ldapConfig;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String credentials = authentication.getCredentials().toString();

        final ResponseEntity<LdapResponse> ldapResponse = loginWithLdap(LdapRequest.builder()
                .username(authentication.getName())
                .password(credentials)
                .build());

        if(!ldapResponse.getStatusCode().equals(HttpStatus.OK)) {
            return null;
        }

        final Set<SimpleGrantedAuthority> grantedRoles = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        final UserDetails userDetails = new User(authentication.getName(), credentials, grantedRoles);
        return new UsernamePasswordAuthenticationToken(userDetails, credentials, grantedRoles);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private ResponseEntity<LdapResponse> loginWithLdap(final LdapRequest ldapRequest){
        final LdapResponse response = LdapResponse.builder()
                .username(ldapRequest.getUsername())
                .build();

        try {
            final LdapContextSource contextSource = new LdapContextSource();
            contextSource.setUrl(this.ldapConfig.getUrl());
            contextSource.setUserDn(ldapRequest.getUsername() + "@" + this.ldapConfig.getDomainName());
            contextSource.setPassword(ldapRequest.getPassword());
            contextSource.afterPropertiesSet();
            contextSource.getReadOnlyContext();
            log.info("User LDAP validated "+ldapRequest.getUsername());
            return ResponseEntity.ok(response);
        }catch (Exception e){
            log.warn("User LDAP invalid "+ldapRequest.getUsername());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
