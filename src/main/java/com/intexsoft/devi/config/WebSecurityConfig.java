package com.intexsoft.devi.config;

import com.intexsoft.devi.exception.JwtAuthEntryPoint;
import com.intexsoft.devi.filter.JwtAuthTokenFilter;
import com.intexsoft.devi.filter.LocaleFilter;
import com.intexsoft.devi.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Specifies that the class contains the definitions
 * and dependencies of the Bean components.
 *
 * @author ilya.korzhavin
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Bean for authenticate
     *
     * @return authenticationManagerBean
     * @throws Exception if will exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Bean for encoding password
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().
                authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers(HttpMethod.POST, "/groups", "/students", "/students/**", "/teachers", "/teachers/**").hasAnyRole("ADMIN", "PM")
                .antMatchers(HttpMethod.PUT, "/groups/**", "/students/**", "/teachers/**").hasAnyRole("ADMIN", "PM")
                .antMatchers(HttpMethod.DELETE, "/groups/**", "/students/**", "/teachers/**").hasRole("ADMIN")
                .anyRequest().hasAnyRole("USER", "ADMIN", "PM")
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(new LocaleFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(new JwtAuthTokenFilter(jwtProvider, userDetailsService), LocaleFilter.class);
    }
}
