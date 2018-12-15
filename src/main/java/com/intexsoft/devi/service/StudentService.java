package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Student;
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
import java.util.Optional;

/**
 * @author DEVIAPHAN
 * Business Logic Service Class
 */
@Service
public class StudentService {
    @Autowired
    StudentsRepository studentsRepository;

    @Autowired
    GroupService groupService;

    @Autowired
    MessageSource messageSource;

    private static final Logger LOGGER =
            LoggerFactory.getLogger(StudentService.class);

    /**
     * @param locale
     * @return getAll student entities in the database.
     */
    public List<Student> getAll(Locale locale) {
        LOGGER.info(messageSource.getMessage("getAll", new Object[]{"students"}, locale));
        return studentsRepository.findAll();
    }

    /**
     * @param id
     * @param locale
     * @return student entity by ID in the database.
     */
    public Student getById(Long id, Locale locale) throws EntityNotFoundException {
        Optional<Student> studentsOptional = studentsRepository.findById(id);
        if (studentsOptional.isPresent()) {
            LOGGER.info(messageSource.getMessage("getById", new Object[]{"student", id}, locale));
            return studentsOptional.get();
        }
        LOGGER.error(messageSource.getMessage("EntityNotFoundException", new Object[]{"Get student by id", id}, locale));
        throw new EntityNotFoundException();
    }

    /**
     * @param student
     * @param groupId
     * @param locale
     * @return added student entity in the database.
     */
    @Transactional
    public Student save(Student student, Long groupId, Locale locale) {
        student.setGroup(groupService.getById(groupId, locale));
        LOGGER.info(messageSource.getMessage("add", new Object[]{"student"}, locale));
        return studentsRepository.save(student);
    }

    /**
     * @param student
     * @param studentId
     * @param groupId
     * @param locale
     * @return updated student entity in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @Transactional
    public Student updateById(Student student, Long studentId, Long groupId, Locale locale) throws EntityNotFoundException {
        Optional<Student> studentsOptional = studentsRepository.findById(studentId);
        if (!studentsOptional.isPresent()) {
            LOGGER.error(messageSource.getMessage("EntityNotFoundException", new Object[]{"Update student by id", studentId}, locale));
            throw new EntityNotFoundException();
        }

        Student currentStudent = studentsOptional.get();
        currentStudent.setFirstName(student.getFirstName());
        currentStudent.setLastName(student.getLastName());
        currentStudent.setGroup(groupService.getById(groupId, locale));
        LOGGER.info(messageSource.getMessage("updateById", new Object[]{"student", studentId}, locale));
        return studentsRepository.save(currentStudent);
    }

    /**
     * @param locale
     * @param id     the student entity to be removed from the database
     */
    public void deleteById(Long id, Locale locale) {
        LOGGER.info(messageSource.getMessage("deletedById", new Object[]{"student", id}, locale));
        studentsRepository.deleteById(id);
    }
}
