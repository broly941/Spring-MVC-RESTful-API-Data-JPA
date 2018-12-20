package com.intexsoft.devi.filter;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

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

    private static final String LANGUAGE_NOT_SUPPORTED = "Language not supported.";
    private static final String LOCALE_EN = "en";
    private static final String PROPERTIES = ".properties";

    private List<String> langList;

    @Override
    protected void initFilterBean() {
        langList = loadLanguageList();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String acceptLanguage = request.getHeader(ACCEPT_LANGUAGE);
        try {
            if (langList.contains(acceptLanguage)) {
                setLocale(request, response, filterChain, acceptLanguage);
            } else if (acceptLanguage == null) {
                setLocale(request, response, filterChain, LOCALE_EN);
            } else {
                response.sendError(400, LANGUAGE_NOT_SUPPORTED);
            }
        } catch (IOException | ServletException e) {
            LocaleContextHolder.resetLocaleContext();
        }
    }

    private void setLocale(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String acceptLanguage) throws IOException, ServletException {
        Locale locale = new Locale.Builder().setLanguage(acceptLanguage).build();
        LocaleContextHolder.setLocale(locale);
        filterChain.doFilter(request, response);
    }

    private List<String> loadLanguageList() {
        Pattern pattern = Pattern.compile("(?<=\\_).+?(?=\\.)");
        try {
            Path paths = Paths.get(Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).toURI());

            return Files.list(paths).filter(path -> path.getFileName().toString().endsWith(PROPERTIES)).map(path -> {
                Matcher matcher = pattern.matcher(path.getFileName().toString());
                if (matcher.find()) {
                    return matcher.group(0);
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (IOException | URISyntaxException e) {
            return null;
        }
    }
}