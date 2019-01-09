package com.intexsoft.devi.filter;

import com.intexsoft.devi.service.GroupServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

/**
 * @author DEVIAPHAN on 19.12.2018
 * @project university
 * If the request has the wrong Accept-Language or Locale is not available then throw 404 error.
 */
public class LocaleResolverRequestFilter extends OncePerRequestFilter {

    private static final Predicate<Integer> indexValidPredicate = index -> index != -1;

    private static final String LANGUAGE_NOT_SUPPORTED = "Language not supported: ";
    private static final String LOCALE_EN = "en";
    private static final String PROPERTIES = ".properties";
    private static final String HTTP_STATUS_400_LANGUAGE_NOT_SUPPORTED = "HTTP Status 400 – Language not supported: ";
    private static final String REGEX = "(?<=\\_).+?(?=\\.)";
    private static final String UTF_8 = "UTF-8";
    private static final String COMMA = ",";
    private static final String MINUS = "-";

    private List<String> langList;

    @Override
    protected void initFilterBean() {
        langList = loadLanguageList();
    }

    @Autowired
    MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String acceptLanguage = substring(request.getHeader(ACCEPT_LANGUAGE), COMMA);
        if (acceptLanguage == null) {
            acceptLanguage = LOCALE_EN;
        }
        try {
            setLanguage(request, response, filterChain, acceptLanguage);
        } catch (IOException | ServletException e) {
            LocaleContextHolder.resetLocaleContext();
            filterChain.doFilter(request, response);
        }
    }

    private void setLanguage(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String acceptLanguage) throws IOException, ServletException {
        if (langList.contains(acceptLanguage)) {
            setLocale(request, response, filterChain, acceptLanguage);
        } else {
            String language = substring(acceptLanguage, MINUS);
            if (isValidLanguage(language)) {
                setLocale(request, response, filterChain, language);
            } else {
                LOGGER.error(HTTP_STATUS_400_LANGUAGE_NOT_SUPPORTED + language);
                response.sendError(400, LANGUAGE_NOT_SUPPORTED + language);
            }
        }
    }

    //TODO выровнять методы по приоритету
    private boolean isValidLanguage(String language) {
        Locale locale = new Locale(language);
        try {
            locale.getISO3Language();
        } catch (final MissingResourceException ex) {
            return false;
        }
        return true;
    }

    private String substring(String value, String delimiter) {
        int index = value.indexOf(delimiter);
        return indexValidPredicate.test(index) ? value.substring(0, index) : value;
    }

    private void setLocale(HttpServletRequest request, HttpServletResponse response, FilterChain
            filterChain, String acceptLanguage) throws IOException, ServletException {
        Locale locale = Locale.forLanguageTag(acceptLanguage);
        LocaleContextHolder.setLocale(locale);
        WebUtils.setSessionAttribute(request, SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
        filterChain.doFilter(request, response);
    }

    private List<String> loadLanguageList() {
        Pattern pattern = Pattern.compile(REGEX);
        try {
            Path paths = Paths.get(Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).toURI());

            return Files.list(paths)
                    .filter(path -> path.getFileName().toString().endsWith(PROPERTIES))
                    .map(path -> {
                        Matcher matcher = pattern.matcher(path.getFileName().toString());
                        if (matcher.find()) {
                            return matcher.group(0);
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException | URISyntaxException e) {
            return null;
        }
    }
}