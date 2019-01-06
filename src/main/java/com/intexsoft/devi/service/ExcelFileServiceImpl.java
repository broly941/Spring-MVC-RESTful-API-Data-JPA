package com.intexsoft.devi.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * @author DEVIAPHAN
 * Business Logic Excel File Class
 */
@Service
public class ExcelFileServiceImpl implements ExcelFileService {

    private static final String DATA_SAVED_SUCCESSFULLY = "Data saved successfully.";
    private static final String VALIDATION_STATUS = "VALIDATION_STATUS";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TaskExecutor taskExecutor;

    @Override
    public String createEntity(Locale locale, MultipartFile file, Integer page, BiPredicate<Map<Integer, List<Object>>, StringBuilder> validation, BiConsumer<Map<Integer, List<Object>>, Locale> save) throws IOException {
        StringBuilder validationStatus = new StringBuilder(messageSource.getMessage(VALIDATION_STATUS, null, locale) + "\n");
        Map<Integer, List<Object>> map = parser(file, page);
        if (validation.test(map, validationStatus)) {
            validationStatus.append(DATA_SAVED_SUCCESSFULLY);
            save.accept(map, locale);
        }
        return validationStatus.toString();
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