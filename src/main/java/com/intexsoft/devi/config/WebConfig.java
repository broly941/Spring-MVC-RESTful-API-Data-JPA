package com.intexsoft.devi.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.nio.charset.StandardCharsets;

/**
 * @author DEVIAPHAN
 * Specifies that the class contains the definitions and dependencies of the Bean components.
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.intexsoft.devi")
public class WebConfig implements WebMvcConfigurer {

    private static final String CLASSPATH_MESSAGES = "classpath:messages";
    private static final String LANG = "lang";

    /**
     * Describes bean for message source, used to logger
     *
     * @return messageSource
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(CLASSPATH_MESSAGES);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return messageSource;
    }

    /**
     * Enable Cross-Origin Resource Sharing
     *
     * @param registry object which can be used for additional configuration
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    /**
     * Describes bean for Locale
     *
     * @return localeResolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }

    /**
     * Describes bean for Locale
     *
     * @return localeChangeInterceptor
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName(LANG);
        return lci;
    }

    /**
     * Intercept requests
     *
     * @param registry interceptors by extending the WebMvcConfigurerAdapter
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * Upload file
     *
     * @return multipartResolver
     */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(100000);
        return multipartResolver;
    }

    /**
     * Data Transfer Object
     *
     * @return ModelMapper
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}