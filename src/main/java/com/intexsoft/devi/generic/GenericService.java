package com.intexsoft.devi.generic;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * @author DEVIAPHAN on 21.12.2018
 * @project university
 */
public interface GenericService<T> {
    T get(Long id, Function<Long, Optional<T>> function, Locale locale, String message, String par1, String excPar1) throws EntityNotFoundException;

    List<T> getAll(Supplier<List<T>> function, Locale locale, String message, String par1);

    List<T> getList(Long id, Function<Long, List<T>> function, Locale locale, String message, String par1, String excPar1) throws EntityNotFoundException;

    @Transactional
    T save(T entity, UnaryOperator<T> function, Locale locale, String message, String par1);

    @Transactional
    T save(T entity, UnaryOperator<T> function, Locale locale, String message, String par1, Long par2);

    void deleteById(Long id, Consumer<Long> function, Locale locale, String message, String par1);
}
