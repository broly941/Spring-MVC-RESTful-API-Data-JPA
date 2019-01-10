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

    private final Predicate<Integer> indexValidPredicate = index -> index != -1;

    private static final String LANGUAGE_NOT_SUPPORTED = "Language not supported: ";
    private static final String LOCALE_EN = "en-US";
    private static final String PROPERTIES = ".properties";
    private static final String HTTP_STATUS_400_LANGUAGE_NOT_SUPPORTED = "HTTP Status 400 – Language not supported: ";
    private static final String REGEX = "(?<=\\_).+?(?=\\.)";
    private static final String COMMA = ",";
    private static final String MINUS = "-";
    private static final String UNDERSCORE = "_";

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
        String acceptLanguage = request.getHeader(ACCEPT_LANGUAGE);
        if (acceptLanguage == null) {
            setLocale(request, response, filterChain, LOCALE_EN);
        } else {
            acceptLanguage = substring(request.getHeader(ACCEPT_LANGUAGE), COMMA);
            try {
                setLanguage(request, response, filterChain, acceptLanguage);
            } catch (IOException | ServletException e) {
                LocaleContextHolder.resetLocaleContext();
                filterChain.doFilter(request, response);
            }
        }
    }

    private void setLocale(HttpServletRequest request, HttpServletResponse response, FilterChain
            filterChain, String acceptLanguage) throws IOException, ServletException {
        Locale locale = Locale.forLanguageTag(acceptLanguage);
        LocaleContextHolder.setLocale(locale);
        WebUtils.setSessionAttribute(request, SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
        filterChain.doFilter(request, response);
    }

    private String substring(String value, String delimiter) {
        int index = value.indexOf(delimiter);
        return indexValidPredicate.test(index) ? value.substring(0, index) : value;
    }

    private void setLanguage(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String acceptLanguage) throws IOException, ServletException {
        if (langList.contains(acceptLanguage.replace('-', '_'))) {
            setLocale(request, response, filterChain, acceptLanguage);
        } else if (!getLocaleByLanguage(request, response, filterChain, acceptLanguage)) {
            LOGGER.error(HTTP_STATUS_400_LANGUAGE_NOT_SUPPORTED + acceptLanguage);
            response.sendError(400, LANGUAGE_NOT_SUPPORTED + acceptLanguage);
        }
    }

    private boolean getLocaleByLanguage(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String acceptLanguage) throws IOException, ServletException {
        String language = substring(acceptLanguage, MINUS);
        for (String value : langList) {
            if (substring(value, UNDERSCORE).contains(language)) {
                setLocale(request, response, filterChain, value.replace("_", "-"));
                return true;
            }
        }
        return false;
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