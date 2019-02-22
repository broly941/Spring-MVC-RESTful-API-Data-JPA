package com.intexsoft.devi.filter;

import com.intexsoft.devi.security.JwtProvider;
import com.intexsoft.devi.service.Impl.security.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Make validating a request and
 */
@Component("jwtFilter")
public class JwtAuthTokenFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthTokenFilter.class);
    public static final String CAN_NOT_SET_USER_AUTHENTICATION_MESSAGE = "Can NOT set user authentication -> Message: {}";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_ = "Bearer ";

    private JwtProvider tokenProvider;
    private UserDetailsService userDetailsService;

    public JwtAuthTokenFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }


    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("123");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String jwt = getJwt(httpRequest);

            if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
                String username = tokenProvider.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            LOGGER.error(CAN_NOT_SET_USER_AUTHENTICATION_MESSAGE, e);
            throw e;
        }

        chain.doFilter(request, response);
    }

    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(BEARER_)) {
            return authHeader.replace(BEARER_, "");
        }

        return null;
    }
}