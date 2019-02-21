package com.intexsoft.devi.config;

import com.intexsoft.devi.exception.JwtAuthEntryPoint;
import com.intexsoft.devi.filter.LocaleResolverRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
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

    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable().
                authorizeRequests()
                .antMatchers("/university/**", "/groups", "/groups/**").hasRole("ADMIN")
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(new LocaleResolverRequestFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
