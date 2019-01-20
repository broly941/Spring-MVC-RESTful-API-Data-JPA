package com.intexsoft.devi.service.Impl;

import com.intexsoft.devi.controller.response.ValidationStatus;
import com.intexsoft.devi.funcInterface.TriFunction;
import com.intexsoft.devi.service.FileService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
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
public class ExcelFileServiceImpl implements FileService {

    private static final String FORMAT_NOT_SUPPORTED = "FORMAT_NOT_SUPPORTED";
    @Autowired
    MessageSource messageSource;

    private static final String UNABLE_TO_UPLOAD_FILE_IS_EMPTY = "UNABLE_TO_UPLOAD_FILE_IS_EMPTY";
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
            throw new EntityNotFoundException(messageSource.getMessage(UNABLE_TO_UPLOAD_FILE_IS_EMPTY, new Object[]{}, locale));
        } else {
            Map<Integer, List<Object>> parsedEntities = null;
            parsedEntities = parse(locale, file, fileExtension);
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
        if (fileExtension.equals("xlsx")) {
            parsedEntities = parseXlsx(file, locale);
        } else {
            throw new EntityNotFoundException(messageSource.getMessage(FORMAT_NOT_SUPPORTED, new Object[]{fileExtension}, locale));
        }
        return parsedEntities;
    }

    private Map<Integer, List<Object>> returnParsedEntitiesIfNotEmpty(Locale locale, Map<Integer, List<Object>> parsedEntities) {
        if (parsedEntities.isEmpty()) {
            throw new EntityNotFoundException(messageSource.getMessage(UNABLE_TO_UPLOAD_FILE_IS_EMPTY, new Object[]{}, locale));
        } else {
            return parsedEntities;
        }
    }

    private Map<Integer, List<Object>> parseXlsx(InputStream file, Locale locale) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        workbook.close();
        return getMap(rowIterator, locale);
    }

    private Map<Integer, List<Object>> getMap(Iterator<Row> rowIterator, Locale locale) {
        int rowIndex = 0;
        Map<Integer, List<Object>> parsedEntities = new HashMap<>();
        rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            List<Object> lineList = new ArrayList<>();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                rowIndex = row.getRowNum();

                switch (cell.getCellType()) {
                    case STRING:
                        lineList.add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        lineList.add(cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        lineList.add(cell.getStringCellValue());
                        break;
                }
            }
            parsedEntities.put(rowIndex, lineList);
        }
        return returnParsedEntitiesIfNotEmpty(locale, parsedEntities);
    }
}