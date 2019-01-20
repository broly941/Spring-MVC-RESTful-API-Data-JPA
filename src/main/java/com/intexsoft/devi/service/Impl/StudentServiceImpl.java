package com.intexsoft.devi.service.Impl;

import com.intexsoft.devi.controller.response.ValidationStatus;
import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.repository.StudentRepository;
import com.intexsoft.devi.service.BaseService;
import com.intexsoft.devi.service.EntitiesValidationService;
import com.intexsoft.devi.service.GroupService;
import com.intexsoft.devi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author DEVIAPHAN
 * Business Logic Service Class
 */
@Service
public class StudentServiceImpl implements StudentService {
    private static final String STUDENT = "student";
    private static final String STUDENTS = "students";
    private static final String UPDATE_STUDENT_BY_ID = "Update student by id";
    private static final String UPDATE_BY_ID = "updateById";
    private static final String DELETED_BY_ID = "deleteById";
    private static final String ADD = "add";
    private static final String GET_STUDENT_BY_ID = "Get student by id";
    private static final String GET_BY_ID = "getById";
    private static final String GET_ALL = "getAll";
    private static final String GET_STUDENTS_OF_GROUP_BY_ID = "getStudentsOfGroupById";
    private static final String GET_STUDENTS_BY_GROUP_ID = "Get students by group id";
    private static final String GROUP_DOES_NOT_EXIST = "GROUP_DOES_NOT_EXIST";
    private static final String STUDENT_ALREADY_EXISTS = "STUDENT_ALREADY_EXISTS";

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BaseService<Student> studentBaseService;

    @Autowired
    private EntitiesValidationService entitiesValidationService;

    private static final Predicate<List<Object>> allowableColumnPredicate = value -> value.size() != 3;
    private static final Predicate<List<Object>> instanceStringPredicate = value -> (value.get(0) instanceof String && value.get(1) instanceof String && value.get(2) instanceof String);

    /**
     * method return all students
     *
     * @param locale of messages
     * @return getAll student entities in the database.
     */
    @Override
    public List<Student> getAll(Locale locale) {
        return studentBaseService.getAll(studentRepository::findAll, locale, GET_ALL, STUDENTS);
    }

    /**
     * method return student by id
     *
     * @param id     of student
     * @param locale of messages
     * @return student entity by ID in the database.
     */
    @Override
    public Student getById(Long id, Locale locale) {
        return studentBaseService.get(id, studentRepository::findById, locale, GET_BY_ID, STUDENT, GET_STUDENT_BY_ID);
    }

    /**
     * method return all students by group id
     *
     * @param id     of entity
     * @param locale of messages
     * @return List of student
     */
    @Override
    public List<Student> getStudentsOfGroupById(Long id, Locale locale) {
        return studentBaseService.getAll(id, studentRepository::findAllByGroup_Id, locale, GET_STUDENTS_OF_GROUP_BY_ID, STUDENTS, GET_STUDENTS_BY_GROUP_ID);
    }

    /**
     * method return student by name
     *
     * @param firstName of entity
     * @param lastName  of entity
     * @return Optional student
     */
    @Override
    public Optional<Student> getByName(String firstName, String lastName) {
        return studentRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    /**
     * method save student and return it
     *
     * @param student entity
     * @param groupId of group
     * @param locale  of messages
     * @return added student entity in the database.
     */
    @Override
    @Transactional
    public Student save(Student student, Long groupId, Locale locale) {
        student.setGroup(groupService.getById(groupId, locale));
        return studentBaseService.save(student, studentRepository::save, locale, ADD, STUDENT);
    }

    /**
     * method save student and return it
     *
     * @param student entity
     * @param locale  of messages
     * @return added student entity in the database.
     */
    @Override
    @Transactional
    public Student save(Student student, Locale locale) {
        return studentBaseService.save(student, studentRepository::save, locale, ADD, STUDENT);
    }

    /**
     * method update student by id and return it
     *
     * @param student   entity
     * @param studentId of student
     * @param groupId   of group
     * @param locale    of messages
     * @return updated student entity in the database.
     */
    @Override
    @Transactional
    public Student updateById(Student student, Long studentId, Long groupId, Locale locale) {
        Student currentStudent = studentBaseService.get(studentId, studentRepository::findById, locale, UPDATE_BY_ID, STUDENT, UPDATE_STUDENT_BY_ID);
        currentStudent.setFirstName(student.getFirstName());
        currentStudent.setLastName(student.getLastName());
        currentStudent.setGroup(groupService.getById(groupId, locale));
        return studentBaseService.save(currentStudent, studentRepository::save, locale, UPDATE_BY_ID, STUDENT, studentId);
    }

    /**
     * method delete student by id
     *
     * @param locale of messages
     * @param id     the student entity to be removed from the database
     */
    @Override
    public void deleteById(Long id, Locale locale) {
        studentBaseService.deleteById(id, studentRepository::deleteById, locale, DELETED_BY_ID, STUDENT);
    }

    /**
     * method return student by name and group name
     *
     * @param firstName of entity
     * @param lastName  of entity
     * @param groupName of entity group
     * @return return student
     */
    @Override
    public Optional<Student> getStudentByNameAndGroupName(String firstName, String lastName, String groupName) {
        return studentRepository.findByFirstNameAndLastNameAndGroup_Number(firstName, lastName, groupName);
    }

    /**
     * method validate parseEntities and returned validationStatus
     *
     * @param parsedEntities map of entities file
     * @param validEntities  map of validation entities
     * @param locale         of message
     * @return validationStatus
     */
    @Override
    public ValidationStatus validate(Map<Integer, List<Object>> parsedEntities, Map<Integer, Object> validEntities, Locale locale) {
        ValidationStatus validationStatus = new ValidationStatus();
        validationStatus.setRowCount(parsedEntities.size());
        validateParsedEntities(parsedEntities, validEntities, locale, validationStatus);
        return validationStatus;
    }

    private void validateParsedEntities(Map<Integer, List<Object>> parsedEntities, Map<Integer, Object> validEntities, Locale locale, ValidationStatus validationStatus) {
        Set<String> duplicateSet = new HashSet<>();
        parsedEntities.forEach((key, value) -> {
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
        });
    }

    private void validateDuplicate(Locale locale, Set<String> duplicateSet, String firstName, String lastName, List<String> rowErrors) {
        if (!duplicateSet.add(firstName + " " + lastName)) {
            rowErrors.add(messageSource.getMessage("DUPLICATE", new Object[]{}, locale));
        }
    }

    private void validateStudent(Locale locale, String firstName, String lastName, List<String> rowErrors) {
        Optional<Student> optionalStudent = getByName(firstName, lastName);
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

    /**
     * Method save entity from map of valid entities
     *
     * @param validEntities of entity in file.
     * @param locale        of messages.
     */
    @Override
    @Transactional
    public void save(Map<Integer, Object> validEntities, Locale locale) {
        Map<Integer, Student> studentEntities = validEntities.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (Student) e.getValue()));
        studentBaseService.saveAll(new ArrayList<>(studentEntities.values()), studentRepository::saveAll, locale, ADD, STUDENTS);
    }
}