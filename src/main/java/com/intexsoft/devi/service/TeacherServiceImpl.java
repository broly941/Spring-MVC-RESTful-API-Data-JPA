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

    private static final String AT_LEAST_3_COLUMNS_REQUIRED = "AT_LEAST_3_COLUMNS_REQUIRED";
    private static final String TEACHER_DOES_NOT_EXIST = "TEACHER_DOES_NOT_EXIST";
    private static final String TEACHER_ALREADY_EXISTS_WITH_GROUP = "TEACHER_ALREADY_EXISTS_WITH_GROUP";
    private static final String GROUP_DOES_NOT_EXIST = "GROUP_DOES_NOT_EXIST";

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
     * @param id     of entity
     * @param locale of messages
     * @return List of teachers
     */
    @Override
    public List<Teacher> getTeachersOfGroupById(Long id, Locale locale) throws EntityNotFoundException {
        return getAll(id, teacherRepository::findAllTeachersOfGroupById, locale, GET_TEACHERS_OF_GROUP_BY_ID, TEACHERS, GET_TEACHERS_OF_GROUP_BY_ID1);
    }

    /**
     * @param firstName of entity
     * @param lastName  of entity
     * @return Optional teacher
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
     * @param map              of entity in file
     * @param validationStatus An object that contains validation information.
     * @param locale           of messages
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
            setErrorAlowableColumns(leastRequaredColumnPredicate, value, locale, row, AT_LEAST_3_COLUMNS_REQUIRED);
            setErrorIfValueNotString(instanceStringPredicate, value, locale, row);

            Optional<Teacher> teacher = getTeacherByName(value.get(0).toString(), value.get(1).toString());
            if (teacher.isPresent()) {
                checkTeacherGroupExists(value, teacher, locale, row);
            } else {
                row.add(messageSource.getMessage(TEACHER_DOES_NOT_EXIST, new Object[]{}, locale));
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

    private void checkTeacherGroupExists(List<Object> value, Optional<Teacher> teacher, Locale locale, List<String> row) {
        for (int i = 2; i < value.size(); i++) {
            Optional<Group> group = groupService.getByNumber(value.get(i).toString());
            if (group.isPresent()) {
                if (!isGroupTeacherExist(group.get().getId(), teacher.get().getId())) {
                    row.add(messageSource.getMessage(TEACHER_ALREADY_EXISTS_WITH_GROUP, new Object[]{group.get().getNumber()}, locale));
                }
            } else {
                row.add(messageSource.getMessage(GROUP_DOES_NOT_EXIST, new Object[]{value.get(i).toString()}, locale));
            }
        }
    }

    /**
     * Method save entity in db
     *
     * @param map    of entity in file
     * @param locale of messages
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
