package com.intexsoft.devi.generic;

import com.intexsoft.devi.beans.ValidationStatus;
import com.intexsoft.devi.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
 * <p>
 * The class is engaged in managing entities in the database.
 */
public abstract class GenericServiceImpl<T> implements GenericService<T> {

    @Autowired
    MessageSource messageSource;

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GroupService.class);

    private static final String ENTITY_NOT_FOUND_EXCEPTION = "EntityNotFoundException";
    private static final String CAN_NOT_FIND_WITH_ID = "Can_not_find_with_id";
    public static final String ROW = "ROW";
    public static final String SOME_TYPE_IS_NOT_A_STRING = "SOME_TYPE_IS_NOT_A_STRING";

    /**
     * Get all entities.
     *
     * @param locale of messages
     * @return getAll group entities in the database.
     */
    @Override
    public List<T> getAll(Supplier<List<T>> function, Locale locale, String message, String par1) {
        LOGGER.info(messageSource.getMessage(message, new Object[]{par1}, locale));
        return function.get();
    }

    /**
     * Get entity by ID.
     *
     * @param id
     * @param function
     * @param locale
     * @return
     * @throws EntityNotFoundException
     */
    @Override
    public T get(Long id, Function<Long, Optional<T>> function, Locale locale, String message, String par1, String excPar1) {
        Optional<T> optional = function.apply(id);
        if (optional.isPresent()) {
            LOGGER.info(messageSource.getMessage(message, new Object[]{par1, id}, locale));
            return optional.get();
        } else {
            LOGGER.error(messageSource.getMessage(ENTITY_NOT_FOUND_EXCEPTION, new Object[]{par1, id}, locale));
            throw new EntityNotFoundException(messageSource.getMessage(CAN_NOT_FIND_WITH_ID, new Object[]{id}, locale));
        }
    }


    /**
     * Get all entities in List.
     *
     * @param id
     * @param function
     * @param locale
     * @return
     * @throws EntityNotFoundException
     */
    @Override
    public List<T> getAll(Long id, Function<Long, List<T>> function, Locale locale, String message, String par1, String excPar1) throws EntityNotFoundException {
        List<T> list = function.apply(id);
        if (list.isEmpty()) {
            LOGGER.error(messageSource.getMessage(ENTITY_NOT_FOUND_EXCEPTION, new Object[]{par1, id}, locale));
            throw new EntityNotFoundException();
        } else {
            LOGGER.info(messageSource.getMessage(message, new Object[]{par1, id}, locale));
            return list;
        }
    }

    /**
     * Accepts an entity and saves it.
     *
     * @param entity
     * @param function
     * @param locale
     * @param message
     * @param par1
     * @return
     */
    @Override
    @Transactional
    public T save(T entity, UnaryOperator<T> function, Locale locale, String message, String par1) {
        LOGGER.info(messageSource.getMessage(message, new Object[]{par1}, locale));
        return function.apply(entity);
    }

    /**
     * Accepts an entity and saves it.
     *
     * @param entity
     * @param function
     * @param locale
     * @param message
     * @param par1
     * @param par2
     * @return
     */
    @Override
    @Transactional
    public T save(T entity, UnaryOperator<T> function, Locale locale, String message, String par1, Long par2) {
        LOGGER.info(messageSource.getMessage(message, new Object[]{par1, par2}, locale));
        return function.apply(entity);
    }

    /**
     * deletes record by id
     *
     * @param id
     * @param function
     * @param locale
     * @param message
     * @param par1
     */
    @Override
    public void deleteById(Long id, Consumer<Long> function, Locale locale, String message, String par1) {
        LOGGER.info(messageSource.getMessage(message, new Object[]{par1, id}, locale));
        function.accept(id);
    }

    /**
     * The method returns the validation status; if validation fails, the values are set.
     *
     * @param map
     * @param validationStatus
     * @param isValid
     * @param validRow
     * @return
     */
    @Override
    public boolean returnValidationStatus(Map<Integer, List<Object>> map, ValidationStatus validationStatus, AtomicBoolean isValid, AtomicInteger validRow, AtomicInteger errorsCount) {
        if (!validationStatus.isValid()) {
            isValid.set(false);
        }

        validationStatus.setRowCount(map.size());
        validationStatus.setValidRow(validRow.get());
        validationStatus.setErrorsCount(errorsCount.get());
        return isValid.get();
    }

    /**
     * If the value in the collection satisfies the predicate condition, an error is added.
     *
     * @param predicate
     * @param value
     * @param locale
     * @param row
     */
    @Override
    public void setErrorIfValueNotString(Predicate<List<Object>> predicate, List<Object> value, Locale locale, List<String> row) {
        if (!predicate.test(value)) {
            row.add(messageSource.getMessage(SOME_TYPE_IS_NOT_A_STRING, new Object[]{}, locale));
        }
    }

    /**
     * If the value in the collection satisfies the predicate condition, an error is added.
     *
     * @param predicate
     * @param value
     * @param locale
     * @param row
     * @param message
     */
    @Override
    public void setErrorAlowableColumns(Predicate<List<Object>> predicate, List<Object> value, Locale locale, List<String> row, String message) {
        if (predicate.test(value)) {
            row.add(messageSource.getMessage(message, new Object[]{}, locale));
        }
    }
}
