package com.intexsoft.devi.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * @author DEVIAPHAN on 03.01.2019
 * @project university
 */
public interface ExcelFileService {
    String createEntity(Locale locale, MultipartFile file, Integer page, BiPredicate<Map<Integer, List<Object>>, StringBuilder> validation, BiConsumer<Map<Integer, List<Object>>, Locale> save) throws IOException, InvalidFormatException;
}
