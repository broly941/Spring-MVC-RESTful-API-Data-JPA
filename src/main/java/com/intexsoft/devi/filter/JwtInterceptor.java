package com.intexsoft.devi.filter;

import com.intexsoft.devi.security.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * get JWT token from header
 * validate JWT
 * parse username from validated JWT
 * load data from users table, then build an authentication object
 * set the authentication object to Security Context
 */
public class JwtInterceptor extends HandlerInterceptorAdapter {

    public static final String CAN_NOT_SET_USER_AUTHENTICATION_MESSAGE = "Can NOT set user authentication -> Message: {}";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_ = "Bearer ";
    public static final String REPLACEMENT = "";

    @Autowired
    private JwtProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String jwt = getJwt(request);
            if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
                String username = tokenProvider.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            LOGGER.error(CAN_NOT_SET_USER_AUTHENTICATION_MESSAGE, e);
            throw e;
        }

        return true;
    }

    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(BEARER_)) {
            return authHeader.replace(BEARER_, REPLACEMENT);
        }
        return null;
    }
}
