package com.intexsoft.devi.generic;

import com.intexsoft.devi.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
public class GenericServiceImpl<T> implements GenericService<T> {

    @Autowired
    MessageSource messageSource;

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GroupService.class);

    private static final String ENTITY_NOT_FOUND_EXCEPTION = "EntityNotFoundException";

    /**
     * @param locale of messages
     * @return getAll group entities in the database.
     */
    @Override
    public List<T> getAll(Supplier<List<T>> function, Locale locale, String message, String par1) {
        LOGGER.info(messageSource.getMessage(message, new Object[]{par1}, locale));
        return function.get();
    }

    /**
     * @param id
     * @param function
     * @param locale
     * @return
     * @throws EntityNotFoundException
     */
    @Override
    public T getById(Long id, Function<Long, Optional<T>> function, Locale locale, String message, String par1, String excPar1) throws EntityNotFoundException {
        Optional<T> optional = function.apply(id);
        if (optional.isPresent()) {
            LOGGER.info(messageSource.getMessage(message, new Object[]{par1, id}, locale));
            return optional.get();
        } else {
            return throwEntityException(excPar1, id, locale);
        }
    }

    @Override
    @Transactional
    public T save(T entity, UnaryOperator<T> function, Locale locale, String message, String par1) {
        LOGGER.info(messageSource.getMessage(message, new Object[]{par1}, locale));
        return function.apply(entity);
    }

    @Override
    @Transactional
    public T save(T entity, UnaryOperator<T> function, Locale locale, String message, String par1, Long par2) {
        LOGGER.info(messageSource.getMessage(message, new Object[]{par1, par2}, locale));
        return function.apply(entity);
    }

    @Override
    public void deleteById(Long id, Consumer<Long> function, Locale locale, String message, String par1) {
        LOGGER.info(messageSource.getMessage(message, new Object[]{par1, id}, locale));
        function.accept(id);
    }

    private T throwEntityException(String par1, Long id, Locale locale) throws EntityNotFoundException {
        LOGGER.error(messageSource.getMessage(ENTITY_NOT_FOUND_EXCEPTION, new Object[]{par1, id}, locale));
        throw new EntityNotFoundException();
    }

}
