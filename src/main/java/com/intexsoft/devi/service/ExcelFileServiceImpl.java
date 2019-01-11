package com.intexsoft.devi.service;

import com.intexsoft.devi.beans.ValidationStatus;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
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
public class ExcelFileServiceImpl implements ExcelFileService {

    private static final String UNABLE_TO_UPLOAD_FILE_IS_EMPTY = "UNABLE_TO_UPLOAD_FILE_IS_EMPTY";
    private static final String VALIDATION_STATUS = "validationStatus";

    @Autowired
    MessageSource messageSource;

    @Autowired
    private WebApplicationContext webAppContext;

    /**
     * The method accepts a file and returns the status
     * of successfully completed validation and storage
     * or a list of errors that need to be fixed.
     *
     * @param locale     of messages
     * @param file       input
     * @param page       of file
     * @param validation Method for file validation
     * @param save       Method for file save
     * @return validation status
     * @throws IOException
     * @throws EntityNotFoundException
     */
    @Override
    public ValidationStatus createEntity(Locale locale, MultipartFile file, Integer page, ThreePridicate<Map<Integer, List<Object>>, ValidationStatus, Locale> validation, BiConsumer<Map<Integer, List<Object>>, Locale> save) throws IOException, EntityNotFoundException {
        if (file == null || file.isEmpty()) {
            throw new EntityNotFoundException(messageSource.getMessage(UNABLE_TO_UPLOAD_FILE_IS_EMPTY, new Object[]{}, locale));
        }
        ValidationStatus validationStatus = webAppContext.getBean(VALIDATION_STATUS, ValidationStatus.class);
        Map<Integer, List<Object>> map = parser(file, page);
        if (validation.test(map, validationStatus, locale)) {
            save.accept(map, locale);
        }
        return validationStatus;
    }

    private Map<Integer, List<Object>> parser(MultipartFile file, Integer page) throws IOException {
        byte[] byteArr = file.getBytes();
        InputStream stream = new ByteArrayInputStream(byteArr);

        XSSFWorkbook workbook = new XSSFWorkbook(stream);
        XSSFSheet sheet = workbook.getSheetAt(page);
        Iterator<Row> rowIterator = sheet.iterator();
        workbook.close();
        return getMap(rowIterator);
    }

    private Map<Integer, List<Object>> getMap(Iterator<Row> rowIterator) {
        int rowIndex = 0;
        Map<Integer, List<Object>> excelMap = new HashMap<>();
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
            excelMap.put(rowIndex, lineList);
        }
        return excelMap;
    }
}