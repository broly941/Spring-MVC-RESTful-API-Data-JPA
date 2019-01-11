package com.intexsoft.devi.generic;

import com.intexsoft.devi.beans.ValidationStatus;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;

/**
 * @author DEVIAPHAN on 21.12.2018
 * @project university
 */
public interface GenericService<T> {
    T get(Long id, Function<Long, Optional<T>> function, Locale locale, String message, String par1, String excPar1) throws Exception;

    List<T> getAll(Supplier<List<T>> function, Locale locale, String message, String par1);

    List<T> getAll(Long id, Function<Long, List<T>> function, Locale locale, String message, String par1, String excPar1) throws EntityNotFoundException;

    @Transactional
    T save(T entity, UnaryOperator<T> function, Locale locale, String message, String par1);

    @Transactional
    T save(T entity, UnaryOperator<T> function, Locale locale, String message, String par1, Long par2);

    void deleteById(Long id, Consumer<Long> function, Locale locale, String message, String par1);


    boolean returnValidationStatus(Map<Integer, List<Object>> map, ValidationStatus validationStatus, AtomicBoolean isValid, AtomicInteger validRow, AtomicInteger errorsCount);

    void setErrorIfValueNotString(Predicate<List<Object>> predicate, List<Object> value, Locale locale, List<String> row);

    void setErrorAlowableColumns(Predicate<List<Object>> predicate, List<Object> value, Locale locale, List<String> row, String message);
}
