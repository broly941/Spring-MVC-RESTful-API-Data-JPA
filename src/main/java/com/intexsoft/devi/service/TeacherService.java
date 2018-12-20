package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Teacher;
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
import java.util.Optional;

/**
 * @author DEVIAPHAN
 * Business Logic Service Class
 */
@Service
public class TeacherService {
    private static final String TEACHER = "teacher";
    private static final String TEACHERS = "teachers";
    private static final String GET_TEACHER_BY_ID = "Get teacher by id";
    private static final String ENTITY_NOT_FOUND_EXCEPTION = "EntityNotFoundException";
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

    private static final Logger LOGGER =
            LoggerFactory.getLogger(TeacherService.class);

    /**
     * @param locale of messages
     * @return getAll teacher entity in the database.
     */
    public List<Teacher> getAll(Locale locale) {
        LOGGER.info(messageSource.getMessage(GET_ALL, new Object[]{TEACHERS}, locale));
        return teacherRepository.findAll();
    }

    /**
     * @param id     of teacher
     * @param locale of messages
     * @return teacher entity by ID in the database.
     * @throws EntityNotFoundException if there is no value
     */
    public Teacher getById(Long id, Locale locale) throws EntityNotFoundException {
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        if (teacherOptional.isPresent()) {
            LOGGER.info(messageSource.getMessage(GET_BY_ID, new Object[]{TEACHER, id}, locale));
            return teacherOptional.get();
        }
        LOGGER.error(messageSource.getMessage(ENTITY_NOT_FOUND_EXCEPTION, new Object[]{GET_TEACHER_BY_ID, id}, locale));
        throw new EntityNotFoundException();
    }

    /**
     * @param teacher entity
     * @param locale  of messages
     * @return added teacher entity in the database.
     */
    @Transactional
    public Teacher save(Teacher teacher, Locale locale) {
        LOGGER.info(messageSource.getMessage(ADD, new Object[]{TEACHER}, locale));
        return teacherRepository.save(teacher);
    }

    /**
     * @param teacher   entity
     * @param teacherId of teacher
     * @param locale    of messages
     * @return updated teacher entity in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @Transactional
    public Teacher updateById(Teacher teacher, Long teacherId, Locale locale) throws EntityNotFoundException {
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);
        if (!teacherOptional.isPresent()) {
            LOGGER.error(messageSource.getMessage(ENTITY_NOT_FOUND_EXCEPTION, new Object[]{UPDATE_TEACHER_BY_ID, teacherId}, locale));
            throw new EntityNotFoundException();
        }

        Teacher currentTeacher = teacherOptional.get();
        currentTeacher.setFirstName(teacher.getFirstName());
        currentTeacher.setLastName(teacher.getLastName());
        LOGGER.info(messageSource.getMessage(UPDATE_BY_ID, new Object[]{TEACHER, teacherId}, locale));
        return teacherRepository.save(currentTeacher);
    }

    /**
     * @param id     the teacher entity to be removed from the database
     * @param locale of messages
     */
    public void deleteById(Long id, Locale locale) {
        LOGGER.info(messageSource.getMessage(DELETED_BY_ID, new Object[]{TEACHER, id}, locale));
        teacherRepository.deleteById(id);
    }
}
