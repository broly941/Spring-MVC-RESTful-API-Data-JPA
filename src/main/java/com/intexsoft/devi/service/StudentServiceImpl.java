package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.generic.GenericServiceImpl;
import com.intexsoft.devi.repository.StudentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Locale;

/**
 * @author DEVIAPHAN
 * Business Logic Service Class
 */
@Service
public class StudentServiceImpl extends GenericServiceImpl<Student> implements StudentService{
    private static final String STUDENT = "student";
    private static final String STUDENTS = "students";
    private static final String UPDATE_STUDENT_BY_ID = "Update student by id";
    private static final String UPDATE_BY_ID = "updateById";
    private static final String DELETED_BY_ID = "deletedById";
    private static final String ADD = "add";
    private static final String GET_STUDENT_BY_ID = "Get student by id";
    private static final String GET_BY_ID = "getById";
    private static final String GET_ALL = "getAll";

    @Autowired
    StudentsRepository studentsRepository;

    @Autowired
    GroupService groupService;

    @Autowired
    MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

    /**
     * @param locale of messages
     * @return getAll student entities in the database.
     */
    @Override
    public List<Student> getAll(Locale locale) {
        return getAll(studentsRepository::findAll, locale, GET_ALL, STUDENTS);
    }

    /**
     * @param id     of student
     * @param locale of messages
     * @return student entity by ID in the database.
     */
    @Override
    public Student getById(Long id, Locale locale) throws EntityNotFoundException {
        return getById(id, studentsRepository::findById, locale, GET_BY_ID, STUDENT, GET_STUDENT_BY_ID);
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
        return save(student, studentsRepository::save, locale, ADD, STUDENT);
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
        Student currentStudent = getById(studentId, studentsRepository::findById, locale, UPDATE_BY_ID, STUDENT, UPDATE_STUDENT_BY_ID);
        currentStudent.setFirstName(student.getFirstName());
        currentStudent.setLastName(student.getLastName());
        currentStudent.setGroup(groupService.getById(groupId, locale));
        return save(currentStudent, studentsRepository::save, locale, UPDATE_BY_ID, STUDENT, studentId);
    }

    /**
     * @param locale of messages
     * @param id     the student entity to be removed from the database
     */
    @Override
    public void deleteById(Long id, Locale locale) {
        deleteById(id, studentsRepository::deleteById, locale, DELETED_BY_ID, STUDENT);
    }
}
