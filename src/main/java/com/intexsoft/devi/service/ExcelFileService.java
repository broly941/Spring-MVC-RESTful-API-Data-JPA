package com.intexsoft.devi.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author DEVIAPHAN on 03.01.2019
 * @project university
 */
public interface ExcelFileService {
    String createEntity(Locale locale, MultipartFile file, Integer page, ThreePridicate<Map<Integer, List<Object>>, StringBuilder, Locale> validation, BiConsumer<Map<Integer, List<Object>>, Locale> save) throws IOException;
}
