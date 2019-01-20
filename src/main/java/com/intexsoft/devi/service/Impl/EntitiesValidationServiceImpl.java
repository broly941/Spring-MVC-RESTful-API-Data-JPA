package com.intexsoft.devi.service.Impl;

import com.intexsoft.devi.controller.response.ValidationStatus;
import com.intexsoft.devi.service.EntitiesValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author DEVIAPHAN on 14.01.2019
 * @project university
 * The class performs file validation.
 */
@Service
public class EntitiesValidationServiceImpl implements EntitiesValidationService {

    @Autowired
    MessageSource messageSource;

    private static final String SOME_TYPE_IS_NOT_A_STRING = "SOME_TYPE_IS_NOT_A_STRING";
    private static final String REQUIRED_COLUMN_MISSING = "Required_Column_Missing";
    private static final String ROW = "ROW";

    /**
     * If the value in the collection satisfies the predicate condition, an error is added.
     *
     * @param valueIsStringPredicate     predicate checks if all elements are string
     * @param HasRequiredColumnPredicate checks if all columns are required
     * @param rowErrors                  list of errors in row
     * @param value                      list of check items
     * @param locale                     of message
     * @return answer meets the conditions or not
     */
    @Override
    public boolean isValueStringAndHasReqColumn(Predicate<List<Object>> valueIsStringPredicate, Predicate<List<Object>> HasRequiredColumnPredicate, List<String> rowErrors, List<Object> value, Locale locale) {
        if (HasRequiredColumnPredicate.test(value)) {
            rowErrors.add(messageSource.getMessage(REQUIRED_COLUMN_MISSING, new Object[]{}, locale));
            return false;
        } else if (!valueIsStringPredicate.test(value)) {
            rowErrors.add(messageSource.getMessage(SOME_TYPE_IS_NOT_A_STRING, new Object[]{}, locale));
            return false;
        } else {
            return true;
        }
    }

    /**
     * method fill validation status depending valid status or not
     * @param validationStatus of map
     * @param key of row
     * @param validEntity of map
     * @param locale of message
     * @param rowErrors of map
     * @param validEntities - map of valid entities of file
     */
    @Override
    public void fillValidationStatus(ValidationStatus validationStatus, Integer key, Object validEntity, Locale locale, List<String> rowErrors, Map<Integer, Object> validEntities) {
        if (rowErrors.isEmpty()) {
            validationStatus.validRowInc();
            validEntities.put(key, validEntity);
        } else {
            String rowLabel = messageSource.getMessage(ROW, new Object[]{key}, locale);
            validationStatus.errorRowInc();
            validationStatus.append(rowLabel + rowErrors);
        }
    }
}
