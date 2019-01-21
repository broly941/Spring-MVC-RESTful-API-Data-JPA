package com.intexsoft.devi.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author DEVIAPHAN on 21.01.2019
 * @project university
 */
public interface XLSXParserService {
    Map<Integer, List<Object>> parseXlsx(InputStream file, Locale locale) throws IOException;
}
