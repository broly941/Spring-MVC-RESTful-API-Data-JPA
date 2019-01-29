package com.intexsoft.devi.config;

import com.intexsoft.devi.filter.LocaleResolverRequestFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * Configure the Dispatcher Servlet.
 * Reference to class implementing interfaceDirectory WebMvcConfigurer.
 * Specifying the request URL to Handler Mapping.
 *
 * @author DEVIAPHAN
 */
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{new LocaleResolverRequestFilter()};
    }
}
