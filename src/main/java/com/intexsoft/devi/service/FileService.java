package com.intexsoft.devi.service;

import com.intexsoft.devi.controller.response.ValidationStatus;
import com.intexsoft.devi.funcInterface.TriFunction;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author DEVIAPHAN on 03.01.2019
 * @project university
 */
public interface FileService {
    ValidationStatus parse(Locale locale, InputStream file, String fileExtension, TriFunction<Map<Integer, List<Object>>, Map<Integer, Object>, Locale, ValidationStatus> validation, BiConsumer<Map<Integer, Object>, Locale> save) throws IOException;
}
