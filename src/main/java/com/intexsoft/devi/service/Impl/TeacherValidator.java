package com.intexsoft.devi.service.Impl;

import com.intexsoft.devi.controller.response.ValidationStatus;
import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.service.EntitiesValidationService;
import com.intexsoft.devi.service.GroupService;
import com.intexsoft.devi.service.StudentService;
import com.intexsoft.devi.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
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
public class TeacherValidator implements Callable<Void> {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private GroupService groupService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    EntitiesValidationService entitiesValidationService;

    @Autowired
    private MessageSource messageSource;

    private static final String TEACHER_ALREADY_EXISTS = "TEACHER_ALREADY_EXISTS";
    private static final String GROUP_DOES_NOT_EXIST = "GROUP_DOES_NOT_EXIST";

    private static final Predicate<List<Object>> leastRequiredColumnPredicate = value -> value.size() < 3;
    private static final Predicate<List<Object>> instanceStringPredicate = value -> value.stream().allMatch(v -> v instanceof String);

    private int key;
    private List<Object> value;
    private ConcurrentHashMap<Integer, Object> validEntities;
    private Locale locale;
    private ValidationStatus validationStatus;
    private CopyOnWriteArraySet<String> duplicateSet;
    private String firstName = null;
    private String lastName = null;
    private List<String> rowErrors = new ArrayList<>();
    private List<Group> groupList = null;

    public void setValue(int key, List<Object> value, ConcurrentHashMap<Integer, Object> validEntities, Locale locale, ValidationStatus validationStatus, CopyOnWriteArraySet<String> duplicateSet) {
        this.key = key;
        this.value = value;
        this.validEntities = validEntities;
        this.locale = locale;
        this.validationStatus = validationStatus;
        this.duplicateSet = duplicateSet;
    }

    @Override
    public Void call() {
        if (entitiesValidationService.isValueStringAndHasReqColumn(instanceStringPredicate, leastRequiredColumnPredicate, rowErrors, value, locale)) {
            firstName = value.get(0).toString();
            lastName = value.get(1).toString();
            validateTeacher();
            groupList = validateGroup();
            validateDuplicate();
        }
        entitiesValidationService.fillValidationStatus(validationStatus, key, new Teacher(firstName, lastName, groupList), locale, rowErrors, validEntities);
        return null;
    }

    private void validateDuplicate() {
        if (!duplicateSet.add(firstName + " " + lastName)) {
            rowErrors.add(messageSource.getMessage("DUPLICATE", new Object[]{}, locale));
        }
    }

    private void validateTeacher() {
        Optional<Teacher> optionalTeacher = teacherService.getTeacherByName(firstName, lastName);
        if (optionalTeacher.isPresent()) {
            rowErrors.add(messageSource.getMessage(TEACHER_ALREADY_EXISTS, new Object[]{}, locale));
        }
    }

    private List<Group> validateGroup() {
        List<Group> groupList = new ArrayList<>();
        for (int i = 2; i < value.size(); i++) {
            Optional<Group> group = groupService.getByNumber(value.get(i).toString());
            if (group.isPresent()) {
                groupList.add(group.get());
            } else {
                rowErrors.add(messageSource.getMessage(GROUP_DOES_NOT_EXIST, new Object[]{value.get(i).toString()}, locale));
            }
        }
        return groupList;
    }
}
