package com.intexsoft.devi.service.Impl;

import com.intexsoft.devi.controller.response.ValidationStatus;
import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.service.EntitiesValidationService;
import com.intexsoft.devi.service.GroupService;
import com.intexsoft.devi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;

/**
 * @author DEVIAPHAN on 1/28/2019
 * @project university
 */
@Component
@Scope("prototype")
public class StudentValidator implements Callable<Void> {
    @Autowired
    private StudentService studentService;

    @Autowired
    private GroupService groupService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    EntitiesValidationService entitiesValidationService;

    @Autowired
    private MessageSource messageSource;

    private static final String STUDENT_ALREADY_EXISTS = "STUDENT_ALREADY_EXISTS";
    private static final String GROUP_DOES_NOT_EXIST = "GROUP_DOES_NOT_EXIST";

    private static final Predicate<List<Object>> allowableColumnPredicate = value -> value.size() != 3;
    private static final Predicate<List<Object>> instanceStringPredicate = value -> value.stream().allMatch(v -> v instanceof String);

    private int key;
    private List<Object> value;
    private ConcurrentHashMap<Integer, Object> validEntities;
    private Locale locale;
    private ValidationStatus validationStatus;
    private CopyOnWriteArraySet<String> duplicateSet;

    public void setValue(int key, List<Object> value, ConcurrentHashMap<Integer, Object> validEntities, Locale locale, ValidationStatus validationStatus, CopyOnWriteArraySet<String> duplicateSet){
        this.key = key;
        this.value = value;
        this.validEntities = validEntities;
        this.locale = locale;
        this.validationStatus = validationStatus;
        this.duplicateSet = duplicateSet;
    }

    @Override
    public Void call() {
        Group group = null;
        String firstName = null;
        String lastName = null;
        String groupName;
        List<String> rowErrors = new ArrayList<>();

        if (entitiesValidationService.isValueStringAndHasReqColumn(instanceStringPredicate, allowableColumnPredicate, rowErrors, value, locale)) {
            firstName = value.get(0).toString();
            lastName = value.get(1).toString();
            groupName = value.get(2).toString();
            validateStudent(locale, firstName, lastName, rowErrors);
            group = validateGroup(locale, groupName, rowErrors);
            validateDuplicate(locale, duplicateSet, firstName, lastName, rowErrors);
        }
        entitiesValidationService.fillValidationStatus(validationStatus, key, new Student(firstName, lastName, group), locale, rowErrors, validEntities);
        return null;
    }

    private void validateDuplicate(Locale locale, CopyOnWriteArraySet<String> duplicateSet, String firstName, String lastName, List<String> rowErrors) {
        if (!duplicateSet.add(firstName + " " + lastName)) {
            rowErrors.add(messageSource.getMessage("DUPLICATE", new Object[]{}, locale));
        }
    }

    private void validateStudent(Locale locale, String firstName, String lastName, List<String> rowErrors) {
        Optional<Student> optionalStudent = studentService.getByName(firstName, lastName);
        if (optionalStudent.isPresent()) {
            rowErrors.add(messageSource.getMessage(STUDENT_ALREADY_EXISTS, new Object[]{}, locale));
        }
    }

    private Group validateGroup(Locale locale, String groupName, List<String> rowErrors) {
        Optional<Group> optionalGroup = groupService.getByNumber(groupName);
        if (!optionalGroup.isPresent()) {
            rowErrors.add(messageSource.getMessage(GROUP_DOES_NOT_EXIST, new Object[]{groupName}, locale));
        }
        return optionalGroup.orElse(null);
    }
}
