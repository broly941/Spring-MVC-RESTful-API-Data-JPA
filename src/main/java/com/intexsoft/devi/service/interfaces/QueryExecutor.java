package com.intexsoft.devi.service;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

public interface QueryExecutor {
   <T> List<T> execute(String sqlQuery, Function<ResultSet, List<T>> function);
}
