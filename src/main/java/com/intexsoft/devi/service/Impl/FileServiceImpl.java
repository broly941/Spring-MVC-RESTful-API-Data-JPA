package com.intexsoft.devi.service.Impl;

import com.intexsoft.devi.controller.response.ValidationStatus;
import com.intexsoft.devi.funcInterface.TriFunction;
import com.intexsoft.devi.service.CSVParserService;
import com.intexsoft.devi.service.FileService;
import com.intexsoft.devi.service.XLSXParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author DEVIAPHAN
 * <p>
 * Business Logic Excel File Class
 * <p>
 * The class is engaged in parsing, validating
 * and saving records from a file in the database.
 */
@Service
public class FileServiceImpl implements FileService {

    private static final String FORMAT_NOT_SUPPORTED = "FORMAT_NOT_SUPPORTED";
    private static final String XLSX = "xlsx";
    private static final String CSV = "csv";

    @Autowired
    MessageSource messageSource;

    @Autowired
    CSVParserService csvParserService;

    @Autowired
    XLSXParserService xlsxParserService;

    private static final String UNABLE_TO_UPLOAD_EMPTY_FILE = "UNABLE_TO_UPLOAD_EMPTY_FILE";

    /**
     * The method accepts a file and returns the status
     * of successfully completed validation and storage
     * or a list of errors that need to be fixed.
     *
     * @param locale of messages
     * @param file   input
     * @param save   Method for file save
     * @return validation status
     * @throws IOException if an exception occurred in the file parsing
     */
    @Override
    public ValidationStatus parse(Locale locale, InputStream file, String fileExtension, TriFunction<Map<Integer, List<Object>>, Map<Integer, Object>, Locale, ValidationStatus> validation, BiConsumer<Map<Integer, Object>, Locale> save) throws IOException {
        if (file.available() == 0) {
            throw new EntityNotFoundException(messageSource.getMessage(UNABLE_TO_UPLOAD_EMPTY_FILE, new Object[]{}, locale));
        } else {
            Map<Integer, List<Object>> parsedEntities = parse(locale, file, fileExtension);
            Map<Integer, Object> validEntities = new HashMap<>();
            ValidationStatus validationStatus = validation.apply(parsedEntities, validEntities, locale);
            if (validationStatus.isValid()) {
                save.accept(validEntities, locale);
            }
            return validationStatus;
        }
    }

    private Map<Integer, List<Object>> parse(Locale locale, InputStream file, String fileExtension) throws IOException {
        Map<Integer, List<Object>> parsedEntities;
        if (fileExtension.equals(XLSX)) {
            parsedEntities = xlsxParserService.parseXlsx(file, locale);
        } else if (fileExtension.equals(CSV)) {
            parsedEntities = csvParserService.parseCsv(file, locale);
        } else {
            throw new EntityNotFoundException(messageSource.getMessage(FORMAT_NOT_SUPPORTED, new Object[]{fileExtension}, locale));
        }
        return parsedEntities;
    }

    /**
     * method return map if it not empty or throw exception
     *
     * @param locale         of message
     * @param parsedEntities map of parsed entities
     * @return map of parsed entities
     */
    @Override
    public Map<Integer, List<Object>> getIfNotEmpty(Locale locale, Map<Integer, List<Object>> parsedEntities) {
        if (parsedEntities.isEmpty()) {
            throw new EntityNotFoundException(messageSource.getMessage(UNABLE_TO_UPLOAD_EMPTY_FILE, new Object[]{}, locale));
        } else {
            return parsedEntities;
        }
    }
}