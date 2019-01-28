package com.intexsoft.devi.service.Impl;

import com.intexsoft.devi.controller.response.ValidationStatus;
import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.service.EntitiesValidationService;
import com.intexsoft.devi.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author DEVIAPHAN on 14.01.2019
 * @project university
 * The class performs file validation.
 */
@Service
public class EntitiesValidationServiceImpl implements EntitiesValidationService {

    private static final String TIMED_OUT = "Timed_out";
    private static final String INVALID_VALIDATION_TYPE = "Invalid_validation_type";
    private static final String STUDENT_VALIDATOR = "studentValidator";
    private static final String TEACHER_VALIDATOR = "teacherValidator";

    private static final String SOME_TYPE_IS_NOT_A_STRING = "SOME_TYPE_IS_NOT_A_STRING";
    private static final String REQUIRED_COLUMN_MISSING = "Required_Column_Missing";
    private static final String ROW = "ROW";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private WebApplicationContext context;

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

    /**
     * If the value in the collection satisfies the predicate condition, an error is added.
     *
     * @param valueIsStringPredicate     predicate checks if all elements are string
     * @param hasRequiredColumnPredicate checks if all columns are required
     * @param rowErrors                  list of errors in row
     * @param value                      list of check items
     * @param locale                     of message
     * @return answer meets the conditions or not
     */
    @Override
    public boolean isValueStringAndHasReqColumn(Predicate<List<Object>> valueIsStringPredicate, Predicate<List<Object>> hasRequiredColumnPredicate, List<String> rowErrors, List<Object> value, Locale locale) {
        if (hasRequiredColumnPredicate.test(value)) {
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
     *
     * @param validationStatus of map
     * @param key              of row
     * @param validEntity      of map
     * @param locale           of message
     * @param rowErrors        of map
     * @param validEntities    - map of valid entities of file
     */
    @Override
    public void fillValidationStatus(ValidationStatus validationStatus, int key, Object validEntity, Locale locale, List<String> rowErrors, ConcurrentHashMap<Integer, Object> validEntities) {
        if (rowErrors.isEmpty()) {
            validationStatus.validRowInc();
            validEntities.put(key, validEntity);
        } else {
            String rowLabel = messageSource.getMessage(ROW, new Object[]{key}, locale);
            validationStatus.errorRowInc();
            validationStatus.append(rowLabel + rowErrors);
        }
    }

    /**
     * method return executor service by count core your system
     *
     * @return ExecutorService
     */
    @Override
    public ExecutorService getExecutorService() {
        int countCore = Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(countCore);
    }

    /**
     * method make validate parsed entities and fill validation status
     *
     * @param parsedEntities   of file
     * @param validEntities    is valid entities from parse entities
     * @param locale           of message
     * @param validationStatus is return obj
     * @param reqType          is type to select tasks generator
     */
    @Override
    public void validateParsedEntities(ConcurrentHashMap<Integer, List<Object>> parsedEntities, ConcurrentHashMap<Integer, Object> validEntities, Locale locale, ValidationStatus validationStatus, Type reqType) {
        CopyOnWriteArraySet<String> duplicateSet = new CopyOnWriteArraySet<>();
        ExecutorService executor = getExecutorService();
        List<? extends Callable<Void>> tasks = setTasks(reqType, parsedEntities, validationStatus, validEntities, locale, duplicateSet);

        try {
            executor.invokeAll(tasks);
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            LOGGER.info(messageSource.getMessage(TIMED_OUT, new Object[]{}, locale));
            executor.shutdownNow();
        } finally {
            executor.shutdown();
        }

        validationStatus.sortErrors();
    }

    private List<? extends Callable<Void>> setTasks(Type reqType, ConcurrentHashMap<Integer, List<Object>> parsedEntities, ValidationStatus validationStatus, ConcurrentHashMap<Integer, Object> validEntities, Locale locale, CopyOnWriteArraySet<String> duplicateSet) {
        if (reqType == Student.class) {
            return getStudentTasks(parsedEntities, validEntities, locale, validationStatus, duplicateSet);
        } else if (reqType == Teacher.class) {
            return getTeacherTasks(parsedEntities, validEntities, locale, validationStatus, duplicateSet);
        } else {
            throw new EntityNotFoundException(messageSource.getMessage(INVALID_VALIDATION_TYPE, new Object[]{}, locale));
        }
    }

    private List<StudentValidator> getStudentTasks(ConcurrentHashMap<Integer, List<Object>> parsedEntities, ConcurrentHashMap<Integer, Object> validEntities, Locale locale, ValidationStatus validationStatus, CopyOnWriteArraySet<String> duplicateSet) {
        return parsedEntities.entrySet().stream()
                .map((m) -> {
                    StudentValidator studentValidator = (StudentValidator) context.getBean(STUDENT_VALIDATOR);
                    studentValidator.setValue(m.getKey(), m.getValue(), validEntities, locale, validationStatus, duplicateSet);
                    return studentValidator;
                })
                .collect(Collectors.toList());
    }

    private List<TeacherValidator> getTeacherTasks(ConcurrentHashMap<Integer, List<Object>> parsedEntities, ConcurrentHashMap<Integer, Object> validEntities, Locale locale, ValidationStatus validationStatus, CopyOnWriteArraySet<String> duplicateSet) {
        return parsedEntities.entrySet().stream()
                .map((m) -> {
                    TeacherValidator teacherValidator = (TeacherValidator) context.getBean(TEACHER_VALIDATOR);
                    teacherValidator.setValue(m.getKey(), m.getValue(), validEntities, locale, validationStatus, duplicateSet);
                    return teacherValidator;
                })
                .collect(Collectors.toList());
    }
}