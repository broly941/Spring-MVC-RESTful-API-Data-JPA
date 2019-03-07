package com.intexsoft.devi.service.interfaces;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

/**
 * @author ilya.korzhavin
 */
public interface QueryExecutor {
    <T> List<T> execute(String sqlQuery, Function<ResultSet, List<T>> function);
}
