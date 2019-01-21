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
public interface CSVParserService {
    Map<Integer, List<Object>> parseCsv(InputStream file, Locale locale) throws IOException;
}
