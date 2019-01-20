package com.intexsoft.devi.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * @author DEVIAPHAN on 21.12.2018
 * @project university
 */
public interface BaseService<T> {
    T get(Long id, Function<Long, Optional<T>> function, Locale locale, String message, String par1, String excPar1);

    List<T> getAll(Supplier<List<T>> function, Locale locale, String message, String par1);

    List<T> getAll(Long id, Function<Long, List<T>> function, Locale locale, String message, String par1, String excPar1);

    @Transactional
    T save(T entity, UnaryOperator<T> function, Locale locale, String message, String par1);

    @Transactional
    T save(T entity, UnaryOperator<T> function, Locale locale, String message, String par1, Long par2);

    @Transactional
    void saveAll(List<T> entities, Consumer<List<T>> function, Locale locale, String message, String par1);

    void deleteById(Long id, Consumer<Long> function, Locale locale, String message, String par1);
}
