package com.intexsoft.devi.service;

import com.intexsoft.devi.controller.response.ValidationStatus;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author DEVIAPHAN on 15.01.2019
 * @project university
 */
public interface EntitiesValidationService {
    boolean isValueStringAndHasReqColumn(Predicate<List<Object>> valueIsStringPredicate, Predicate<List<Object>> HasRequiredColumnPredicate, List<java.lang.String> rowErrors, List<Object> value, Locale locale);

    void fillValidationStatus(ValidationStatus validationStatus, Integer key, Object validEntity, Locale locale, List<java.lang.String> rowErrors, Map<Integer, Object> validEntities);
}
