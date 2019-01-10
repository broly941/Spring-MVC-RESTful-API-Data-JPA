package com.intexsoft.devi.service;

import com.intexsoft.devi.beans.ValidationStatus;
import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.generic.GenericServiceImpl;
import com.intexsoft.devi.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
public class TeacherServiceImpl extends GenericServiceImpl<Teacher> implements TeacherService {
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

    private static final String GROUP = " group ";
    private static final String DOES_NOT_EXIST = " does not exist.\n";

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private MessageSource messageSource;

    private static final Predicate<List<Object>> leastRequaredColumnPredicate = value -> value.size() < 3;
    private static final Predicate<List<Object>> instanceStringPredicate = value -> value.stream().allMatch(v -> v instanceof String);

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
        return get(id, teacherRepository::findById, locale, GET_BY_ID, TEACHER, GET_TEACHER_BY_ID);
    }

    /**
     * @param id
     * @param locale
     * @return
     */
    @Override
    public List<Teacher> getTeachersOfGroupById(Long id, Locale locale) throws EntityNotFoundException {
        return getAll(id, teacherRepository::findAllTeachersOfGroupById, locale, GET_TEACHERS_OF_GROUP_BY_ID, TEACHERS, GET_TEACHERS_OF_GROUP_BY_ID1);
    }

    /**
     * @param firstName
     * @param lastName
     * @return
     */
    @Override
    public Optional<Teacher> getTeacherByName(String firstName, String lastName) {
        return teacherRepository.findByFirstNameAndLastName(firstName, lastName);
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
     * @param teacher entity
     * @param locale  of messages
     * @return updated teacher entity in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @Override
    @Transactional
    public Teacher updateById(Teacher teacher, Long teacherId, Locale locale) throws EntityNotFoundException {
        Teacher currentTeacher = get(teacherId, teacherRepository::findById, locale, UPDATE_BY_ID, TEACHER, UPDATE_TEACHER_BY_ID);
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

    /**
     * @param map
     * @param validationStatus
     */
    @Override
    public boolean fileValidation(Map<Integer, List<Object>> map, ValidationStatus validationStatus, Locale locale) {
        AtomicBoolean isValid = new AtomicBoolean(true);
        AtomicInteger errorCount = new AtomicInteger(0);
        map.forEach((key, value) -> {
            if (leastRequaredColumnPredicate.test(value)) {
                validationStatus.append(messageSource.getMessage("AT_LEAST_3_COLUMNS_REQUIRED", new Object[]{key}, locale));
                setValidationFalse(errorCount, isValid);
            } else if (!instanceStringPredicate.test(value)) {
                validationStatus.append(messageSource.getMessage("SOME_TYPE_IS_NOT_A_STRING", new Object[]{key}, locale));
                setValidationFalse(errorCount, isValid);
            } else {
                Optional<Teacher> teacher = getTeacherByName(value.get(0).toString(), value.get(1).toString());
                if (teacher.isPresent()) {
                    checkExists(validationStatus, isValid, key, value, teacher, locale, errorCount);
                } else {
                    validationStatus.append(messageSource.getMessage("TEACHER_DOES_NOT_EXIST", new Object[]{key}, locale));
                    setValidationFalse(errorCount, isValid);
                }
            }
        });

        if (isValid.get() == false) {
            validationStatus.setErrorCount(errorCount.get());
        }
        return isValid.get();
    }

    private void setValidationFalse(AtomicInteger errorCount, AtomicBoolean isValid) {
        errorCount.getAndIncrement();
        isValid.set(false);
    }

    private void checkExists(ValidationStatus validationStatus, AtomicBoolean isValid, Integer key, List<Object> value, Optional<Teacher> teacher, Locale locale, AtomicInteger errorCount) {
        for (int i = 2; i < value.size(); i++) {
            Optional<Group> group = groupService.getByNumber(value.get(i).toString());
            if (group.isPresent()) {
                if (!isGroupTeacherExist(group.get().getId(), teacher.get().getId())) {
                    validationStatus.append(messageSource.getMessage("TEACHER_ALREADY_EXISTS_WITH_GROUP", new Object[]{key, group.get().getNumber()}, locale));
                    setValidationFalse(errorCount, isValid);
                    break;
                }
            } else {
                validationStatus.append(messageSource.getMessage("DOES_NOT_EXIST", new Object[]{key, value.get(i).toString()}, locale));
                setValidationFalse(errorCount, isValid);
                break;
            }
        }
    }

    /**
     * @param map
     * @param locale
     */
    @Override
    public void fileSave(Map<Integer, List<Object>> map, Locale locale) {
        map.forEach((key, value) -> {
            String firstName = value.get(0).toString();
            String lastName = value.get(1).toString();

            List<Group> groupList = new ArrayList<>();
            for (int i = 2; i < value.size(); i++) {
                String groupName = value.get(i).toString();
                Group group = groupService.getByNumber(groupName).get();
                groupList.add(group);
            }

            Teacher teacher = getTeacherByName(firstName, lastName).get();
            teacher.setGroups(groupList);
            save(teacher, locale);
        });
    }

    private boolean isGroupTeacherExist(long groupId, long teacherId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List list = entityManager.createNativeQuery(
                "SELECT * FROM groupteacher WHERE groupteacher.GroupId = ? and groupteacher.TeacherId = ?")
                .setParameter(1, groupId)
                .setParameter(2, teacherId)
                .getResultList();
        return list.isEmpty();
    }
}
