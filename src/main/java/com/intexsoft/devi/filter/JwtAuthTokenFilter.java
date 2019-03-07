package com.intexsoft.devi.filter;

import com.intexsoft.devi.security.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Validate jwt token and set context
 */
public class JwtAuthTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthTokenFilter.class);
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_ = "Bearer ";

    private JwtProvider tokenProvider;
    private UserDetailsService userDetailsService;

    public JwtAuthTokenFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * method make token from request and check it
     * if token is not valid throw exception
     * if token is not exist just make doFilter()
     *
     * @param httpRequest  of user
     * @param httpServletResponse of app
     * @param chain    is filters chain
     * @throws IOException      if will exception
     * @throws ServletException if will exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpServletResponse, FilterChain chain) throws ServletException, IOException {
        String jwt = getJwt(httpRequest);
        LOGGER.info(httpRequest.getRequestURL().toString());
        try {
            if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
                String username = tokenProvider.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            throw e;
        }
        chain.doFilter(httpRequest, httpServletResponse);
    }

    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(BEARER_)) {
            return authHeader.replace(BEARER_, "");
        }

        return null;
    }
}