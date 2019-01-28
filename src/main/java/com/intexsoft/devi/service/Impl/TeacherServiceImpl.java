package com.intexsoft.devi.service.Impl;

import com.intexsoft.devi.controller.response.ValidationStatus;
import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.repository.TeacherRepository;
import com.intexsoft.devi.service.BaseService;
import com.intexsoft.devi.service.EntitiesValidationService;
import com.intexsoft.devi.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author DEVIAPHAN
 * Business Logic Service Class
 */
@Service
public class TeacherServiceImpl implements TeacherService {
    private static final String TEACHER = "teacher";
    private static final String TEACHERS = "teachers";
    private static final String GET_TEACHER_BY_ID = "Get teacher by id";
    private static final String UPDATE_TEACHER_BY_ID = "Update teacher by id";
    private static final String DELETED_BY_ID = "deleteById";
    private static final String UPDATE_BY_ID = "updateById";
    private static final String ADD = "add";
    private static final String GET_BY_ID = "getById";
    private static final String GET_ALL = "getAll";
    private static final String GET_TEACHERS_OF_GROUP_BY_ID = "getTeachersOfGroupById";
    private static final String GET_TEACHERS_OF_GROUP_BY_ID1 = "Get teachers of group by id";

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private BaseService<Teacher> teacherBaseService;

    @Autowired
    private EntitiesValidationService entitiesValidationService;

    /**
     * method return all teachers
     *
     * @param locale of messages
     * @return getAll teachers entities in the database.
     */
    @Override
    public List<Teacher> getAll(Locale locale) {
        return teacherBaseService.getAll(teacherRepository::findAll, locale, GET_ALL, TEACHERS);
    }

    /**
     * method return teacher by id
     *
     * @param id     of teacher
     * @param locale of messages
     * @return teacher entity by ID in the database.
     */
    @Override
    public Teacher getById(Long id, Locale locale) {
        return teacherBaseService.get(id, teacherRepository::findById, locale, GET_BY_ID, TEACHER, GET_TEACHER_BY_ID);
    }

    /**
     * method return all teacher by group id
     *
     * @param id     of entity
     * @param locale of messages
     * @return List of teachers
     */
    @Override
    public List<Teacher> getTeachersOfGroupById(Long id, Locale locale) {
        return teacherBaseService.getAll(id, teacherRepository::findAllTeachersOfGroupById, locale, GET_TEACHERS_OF_GROUP_BY_ID, TEACHERS, GET_TEACHERS_OF_GROUP_BY_ID1);
    }

    /**
     * method return teacher by name
     *
     * @param firstName of entity
     * @param lastName  of entity
     * @return Optional teacher
     */
    @Override
    public Optional<Teacher> getTeacherByName(String firstName, String lastName) {
        return teacherRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    /**
     * method save teacher and return it
     *
     * @param teacher entity
     * @param locale  of messages
     * @return added teacher entity in the database.
     */
    @Override
    @Transactional
    public Teacher save(Teacher teacher, Locale locale) {
        return teacherBaseService.save(teacher, teacherRepository::save, locale, ADD, TEACHER);
    }

    /**
     * method update teacher by id and return it
     *
     * @param teacher entity
     * @param locale  of messages
     * @return updated teacher entity in the database.
     */
    @Override
    @Transactional
    public Teacher updateById(Teacher teacher, Long teacherId, Locale locale) {
        Teacher currentTeacher = teacherBaseService.get(teacherId, teacherRepository::findById, locale, UPDATE_BY_ID, TEACHER, UPDATE_TEACHER_BY_ID);
        currentTeacher.setFirstName(teacher.getFirstName());
        currentTeacher.setLastName(teacher.getLastName());
        return teacherBaseService.save(currentTeacher, teacherRepository::save, locale, UPDATE_BY_ID, TEACHER, teacherId);
    }

    /**
     * method delete teacher by id
     *
     * @param id     the teacher entity to be removed from the database
     * @param locale of messages
     */
    @Override
    public void deleteById(Long id, Locale locale) {
        teacherBaseService.deleteById(id, teacherRepository::deleteById, locale, DELETED_BY_ID, TEACHER);
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
    public ValidationStatus validate(ConcurrentHashMap<Integer, List<Object>> parsedEntities, ConcurrentHashMap<Integer, Object> validEntities, Locale locale) {
        ValidationStatus validationStatus = new ValidationStatus();
        validationStatus.setRowCount(parsedEntities.size());
        entitiesValidationService.validateParsedEntities(parsedEntities, validEntities, locale, validationStatus, Teacher.class);
        return validationStatus;
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
        Map<Integer, Teacher> teacherEntities = validEntities.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (Teacher) e.getValue()));
        teacherBaseService.saveAll(new ArrayList<>(teacherEntities.values()), teacherRepository::saveAll, locale, ADD, TEACHERS);
    }
}
