package com.ooredoo.customerreport.config;

import com.ooredoo.customerreport.model.User;
import com.ooredoo.customerreport.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@Component
@AllArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        /**
         * JWT token is in form "Bearer token"
         * Remove Bearer word and get token only
         */
        if(null != requestTokenHeader
            && requestTokenHeader.startsWith("Bearer ")) {

            jwtToken = requestTokenHeader.substring(7);
            try{
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            }catch (IllegalArgumentException e){
                log.error("Unable to get JWT Token");
            }catch (ExpiredJwtException e){
                log.error("Jwt Token has expired");
            }
        } else {
            log.warn("JWT Token invalid");
        }

        //Once token validated
        if(null != username
                && null == SecurityContextHolder.getContext().getAuthentication()){
            final User userDetails = jwtUserDetailsService.loadUserByUsername(username);

            //if token validated, configure Spring security to manual set authentication
            if(jwtTokenUtil.validateToken(jwtToken, userDetails)){
                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
                        );
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                            .buildDetails(request)
                );

                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext()
                        .setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
