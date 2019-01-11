package com.intexsoft.devi.service;

import com.intexsoft.devi.beans.ValidationStatus;
import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.generic.GenericServiceImpl;
import com.intexsoft.devi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * @author DEVIAPHAN
 * Business Logic Service Class
 */
@Service
public class StudentServiceImpl extends GenericServiceImpl<Student> implements StudentService {
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
    private static final String EXCEEDED_ALLOWABLE_COLUMN_SIZE = "EXCEEDED_ALLOWABLE_COLUMN_SIZE";
    private static final String ALREADY_EXISTS = "ALREADY_EXISTS";
    private static final String GROUP_DOES_NOT_EXIST = "GROUP_DOES_NOT_EXIST";
    private static final String STUDENT_ALREADY_EXISTS = "STUDENT_ALREADY_EXISTS";

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private MessageSource messageSource;

    private static final Predicate<List<Object>> allowableColumnPredicate = value -> value.size() > 3;
    private static final Predicate<List<Object>> instanceStringPredicate = value -> (value.get(0) instanceof String && value.get(1) instanceof String && value.get(2) instanceof String);

    /**
     * @param locale of messages
     * @return getAll student entities in the database.
     */
    @Override
    public List<Student> getAll(Locale locale) {
        return getAll(studentRepository::findAll, locale, GET_ALL, STUDENTS);
    }

    /**
     * @param id     of student
     * @param locale of messages
     * @return student entity by ID in the database.
     */
    @Override
    public Student getById(Long id, Locale locale) throws EntityNotFoundException {
        return get(id, studentRepository::findById, locale, GET_BY_ID, STUDENT, GET_STUDENT_BY_ID);
    }

    /**
     * @param id     of entity
     * @param locale of messages
     * @return List of student
     */
    @Override
    public List<Student> getStudentsOfGroupById(Long id, Locale locale) {
        return getAll(id, studentRepository::findAllByGroup_Id, locale, GET_STUDENTS_OF_GROUP_BY_ID, STUDENTS, GET_STUDENTS_BY_GROUP_ID);
    }

    /**
     * @param firstName of entity
     * @param lastName  of entity
     * @return Optional student
     */
    @Override
    public Optional<Student> getByName(String firstName, String lastName) {
        return studentRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    /**
     * @param student entity
     * @param groupId of group
     * @param locale  of messages
     * @return added student entity in the database.
     */
    @Override
    @Transactional
    public Student save(Student student, Long groupId, Locale locale) {
        student.setGroup(groupService.getById(groupId, locale));
        return save(student, studentRepository::save, locale, ADD, STUDENT);
    }

    /**
     * @param student entity
     * @param locale  of messages
     * @return added student entity in the database.
     */
    @Override
    @Transactional
    public Student save(Student student, Locale locale) {
        return save(student, studentRepository::save, locale, ADD, STUDENT);
    }

    /**
     * @param student   entity
     * @param studentId of student
     * @param groupId   of group
     * @param locale    of messages
     * @return updated student entity in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @Override
    @Transactional
    public Student updateById(Student student, Long studentId, Long groupId, Locale locale) throws EntityNotFoundException {
        Student currentStudent = get(studentId, studentRepository::findById, locale, UPDATE_BY_ID, STUDENT, UPDATE_STUDENT_BY_ID);
        currentStudent.setFirstName(student.getFirstName());
        currentStudent.setLastName(student.getLastName());
        currentStudent.setGroup(groupService.getById(groupId, locale));
        return save(currentStudent, studentRepository::save, locale, UPDATE_BY_ID, STUDENT, studentId);
    }

    /**
     * @param locale of messages
     * @param id     the student entity to be removed from the database
     */
    @Override
    public void deleteById(Long id, Locale locale) {
        deleteById(id, studentRepository::deleteById, locale, DELETED_BY_ID, STUDENT);
    }

    /**
     * @param firstName of entity
     * @param lastName  of entity
     * @param groupName of entity group
     * @return answer is group exist or not
     */
    @Override
    public boolean isStudentGroupExist(String firstName, String lastName, String groupName) {
        return studentRepository.findByFirstNameAndLastNameAndGroup_Number(firstName, lastName, groupName).isPresent();
    }

    /**
     * @param map              of entity in file
     * @param validationStatus An object that contains validation information.
     * @return validation status
     */
    @Override
    public boolean fileValidation(Map<Integer, List<Object>> map, ValidationStatus validationStatus, Locale locale) {
        AtomicBoolean isValid = new AtomicBoolean(true);
        AtomicInteger validRow = new AtomicInteger(0);
        AtomicInteger errorsCount = new AtomicInteger(0);
        map.forEach((key, value) -> {
            String rowNumber = messageSource.getMessage(ROW, new Object[]{key}, locale);
            List<String> row = new ArrayList<>();
            setErrorAlowableColumns(allowableColumnPredicate, value, locale, row, EXCEEDED_ALLOWABLE_COLUMN_SIZE);
            setErrorIfValueNotString(instanceStringPredicate, value, locale, row);

            if (isStudentGroupExist(value.get(0).toString(), value.get(1).toString(), value.get(2).toString())) {
                row.add(messageSource.getMessage(ALREADY_EXISTS, new Object[]{}, locale));
            }
            if (!groupService.getByNumber(value.get(2).toString()).isPresent()) {
                row.add(messageSource.getMessage(GROUP_DOES_NOT_EXIST, new Object[]{value.get(2).toString()}, locale));
            }
            if (getByName(value.get(0).toString(), value.get(1).toString()).isPresent()) {
                row.add(messageSource.getMessage(STUDENT_ALREADY_EXISTS, new Object[]{}, locale));
            }
            if (row.isEmpty()) {
                validRow.getAndIncrement();
            } else {
                errorsCount.getAndIncrement();
                validationStatus.append(rowNumber + row);
            }
        });

        return returnValidationStatus(map, validationStatus, isValid, validRow, errorsCount);
    }

    /**
     * Method save entity in db.
     *
     * @param map    of entity in file.
     * @param locale of messages.
     */
    @Override
    public void fileSave(Map<Integer, List<Object>> map, Locale locale) {
        map.forEach((key, value) -> {
            String firstName = value.get(0).toString();
            String lastName = value.get(1).toString();
            String groupName = value.get(2).toString();

            Group group = groupService.getByNumber(groupName).get();
            Student student = new Student(firstName, lastName, group);
            save(student, locale);
        });
    }
}
