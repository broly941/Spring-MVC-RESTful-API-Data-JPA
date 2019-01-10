package com.intexsoft.devi.service;

import com.intexsoft.devi.beans.ValidationStatus;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author DEVIAPHAN
 * Business Logic Excel File Class
 */
@Service
public class ExcelFileServiceImpl implements ExcelFileService {


    @Autowired
    private MessageSource messageSource;

    @Override
    public ValidationStatus createEntity(Locale locale, MultipartFile file, Integer page, ThreePridicate<Map<Integer, List<Object>>, ValidationStatus, Locale> validation, BiConsumer<Map<Integer, List<Object>>, Locale> save) throws IOException, EntityNotFoundException {
        if (file.isEmpty()) {
            throw new EntityNotFoundException("Unable to upload. File is empty.");
        }
        ValidationStatus validationStatus = new ValidationStatus();
        validationStatus.append(messageSource.getMessage("VALIDATION_STATUS", null, locale));
        Map<Integer, List<Object>> map = parser(file, page);
        if (validation.test(map, validationStatus, locale)) {
            validationStatus.append(messageSource.getMessage("DATA_SAVED_SUCCESSFULLY", null, locale));
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