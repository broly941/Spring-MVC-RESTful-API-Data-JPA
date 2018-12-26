package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.generic.GenericServiceImpl;
import com.intexsoft.devi.repository.TeacherRepository;
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
public class TeacherServiceImpl extends GenericServiceImpl<Teacher> implements TeacherService {
    private static final String TEACHER = "teacher";
    private static final String TEACHERS = "teachers";
    private static final String GET_TEACHER_BY_ID = "Get teacher by id";
    private static final String UPDATE_TEACHER_BY_ID = "Update teacher by id";
    private static final String DELETED_BY_ID = "deletedById";
    private static final String UPDATE_BY_ID = "updateById";
    private static final String ADD = "add";
    private static final String GET_BY_ID = "getById";
    private static final String GET_ALL = "getAll";

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherServiceImpl.class);

    /**
     * @param locale of messages
     * @return getAll teacher entity in the database.
     */
    @Override
    public List<Teacher> getAll(Locale locale) {
        return getAll(teacherRepository::findAll, locale, GET_ALL, TEACHERS);
    }

    /**
     * @param id     of teacher
     * @param locale of messages
     * @return teacher entity by ID in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @Override
    public Teacher getById(Long id, Locale locale) throws EntityNotFoundException {
        return getById(id, teacherRepository::findById, locale, GET_BY_ID, TEACHER, GET_TEACHER_BY_ID);
    }

    /**
     * @param teacher entity
     * @param locale  of messages
     * @return added teacher entity in the database.
     */
    @Override
    @Transactional
    public Teacher save(Teacher teacher, Locale locale) {
        return save(teacher, teacherRepository::save, locale, ADD, TEACHER);
    }

    /**
     * @param teacher   entity
     * @param teacherId of teacher
     * @param locale    of messages
     * @return updated teacher entity in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @Override
    @Transactional
    public Teacher updateById(Teacher teacher, Long teacherId, Locale locale) throws EntityNotFoundException {
        Teacher currentTeacher = getById(teacherId, teacherRepository::findById, locale, UPDATE_BY_ID, TEACHER, UPDATE_TEACHER_BY_ID);
        currentTeacher.setFirstName(teacher.getFirstName());
        currentTeacher.setLastName(teacher.getLastName());
        return save(currentTeacher, teacherRepository::save, locale, UPDATE_BY_ID, TEACHER, teacherId);
    }

    /**
     * @param id     the teacher entity to be removed from the database
     * @param locale of messages
     */
    @Override
    public void deleteById(Long id, Locale locale) {
        deleteById(id, teacherRepository::deleteById, locale, DELETED_BY_ID, TEACHER);
    }
}
