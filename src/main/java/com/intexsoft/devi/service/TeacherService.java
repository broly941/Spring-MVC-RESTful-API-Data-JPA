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
        LOGGER.info(messageSource.getMessage("getAll", new Object[]{"teacher"}, locale));
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
            LOGGER.info(messageSource.getMessage("getById", new Object[]{"teacher", id}, locale));
            return teacherOptional.get();
        }
        LOGGER.error(messageSource.getMessage("EntityNotFoundException", new Object[]{"Get teacher by id", id}, locale));
        throw new EntityNotFoundException();
    }

    /**
     * @param teacher entity
     * @param locale  of messages
     * @return added teacher entity in the database.
     */
    @Transactional
    public Teacher save(Teacher teacher, Locale locale) {
        LOGGER.info(messageSource.getMessage("add", new Object[]{"teacher"}, locale));
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
            LOGGER.error(messageSource.getMessage("EntityNotFoundException", new Object[]{"Update teacher by id", teacherId}, locale));
            throw new EntityNotFoundException();
        }

        Teacher currentTeacher = teacherOptional.get();
        currentTeacher.setFirstName(teacher.getFirstName());
        currentTeacher.setLastName(teacher.getLastName());
        LOGGER.info(messageSource.getMessage("updateById", new Object[]{"teacher", teacherId}, locale));
        return teacherRepository.save(currentTeacher);
    }

    /**
     * @param id     the teacher entity to be removed from the database
     * @param locale of messages
     */
    public void deleteById(Long id, Locale locale) {
        LOGGER.info(messageSource.getMessage("deletedById", new Object[]{"teacher", id}, locale));
        teacherRepository.deleteById(id);
    }
}
