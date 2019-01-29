package com.intexsoft.devi.service.Impl.entityManagment;

import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.repository.GroupRepository;
import com.intexsoft.devi.service.BaseService;
import com.intexsoft.devi.service.GroupService;
import com.intexsoft.devi.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Business Logic Service Class
 *
 * @author DEVIAPHAN
 */
@Service
public class GroupServiceImpl implements GroupService {

    private static final String GROUP = "group";
    private static final String GROUPS = "groups";
    private static final String UPDATE_GROUP_BY_ID = "Update group by id";
    private static final String GET_GROUP_BY_ID = "Get group by id";
    private static final String GET_BY_ID = "getById";
    private static final String GET_ALL = "getAll";
    private static final String ADD = "add";
    private static final String UPDATE_BY_ID = "updateById";
    private static final String DELETED_BY_ID = "deleteById";
    private static final String GET_GROUPS_BY_TEACHER_ID = "getGroupsByTeacherId";
    private static final String GET_GROUPS_BY_TEACHER_ID_TEXT = "Get groups by teacher id";

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private BaseService<Group> groupBaseService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

    /**
     * method return all groups
     *
     * @param locale of messages
     * @return getAll group entities in the database.
     */
    @Override
    public List<Group> getAll(Locale locale) {
        return groupBaseService.getAll(groupRepository::findAll, locale, GET_ALL, GROUPS);
    }

    /**
     * method return a group by id
     *
     * @param id     of group
     * @param locale of messages
     * @return group entity by ID in the database.
     */
    @Override
    public Group getById(Long id, Locale locale) {
        return groupBaseService.get(id, groupRepository::findById, locale, GET_BY_ID, GROUP, GET_GROUP_BY_ID);
    }

    /**
     * method return a group by number
     *
     * @param groupName of entity
     * @return Optional group
     */
    @Override
    public Optional<Group> getByNumber(String groupName) {
        return groupRepository.findByNumber(groupName);
    }

    /**
     * method return a groups by teacher id
     *
     * @param id     of entity
     * @param locale of messages
     * @return list of groups
     */
    @Override
    public List<Group> getGroupsOfTeacherById(Long id, Locale locale) {
        return groupBaseService.getAll(id, groupRepository::findAllGroupsOfTeacherById, locale, GET_GROUPS_BY_TEACHER_ID, GROUPS, GET_GROUPS_BY_TEACHER_ID_TEXT);
    }

    /**
     * method save group and return it
     *
     * @param group         entity
     * @param curatorId     of teacher
     * @param teacherIdList consist of teachers
     * @param locale        of messages
     * @return added group entity in the database.
     */
    @Override
    @Transactional
    public Group save(Group group, Long curatorId, Long[] teacherIdList, Locale locale) {
        group.setTeacher(teacherService.getById(curatorId, locale));
        group.setTeachers(getTeacherList(teacherIdList, locale));
        return groupBaseService.save(group, groupRepository::save, locale, ADD, GROUP);
    }

    /**
     * method update group by id and return it
     *
     * @param group         entity
     * @param groupId       of group
     * @param curatorId     of teacher
     * @param teacherIdList consist of teachers
     * @param locale        of messages
     * @return updated group entity in the database.
     */
    @Override
    @Transactional
    public Group updateById(Group group, Long groupId, Long curatorId, Long[] teacherIdList, Locale locale) {
        Group currentGroup = groupBaseService.get(groupId, groupRepository::findById, locale, UPDATE_BY_ID, GROUP, UPDATE_GROUP_BY_ID);
        currentGroup.setNumber(group.getNumber());
        currentGroup.setTeacher(teacherService.getById(curatorId, locale));
        currentGroup.setTeachers(getTeacherList(teacherIdList, locale));
        return groupBaseService.save(currentGroup, groupRepository::save, locale, UPDATE_BY_ID, GROUP, groupId);
    }

    /**
     * method delete group by id
     *
     * @param locale of messages
     * @param id     the group entity to be removed from the database
     */
    @Override
    @Transactional
    public void deleteById(Long id, Locale locale) {
        EntityManager em = entityManagerFactory.createEntityManager();

        Query query = em.createNativeQuery("DELETE FROM groupofuniversity WHERE groupofuniversity.GroupId = ?");
        query.setParameter(1, id);
        em.joinTransaction();
        query.executeUpdate();

        LOGGER.info(messageSource.getMessage(DELETED_BY_ID, new Object[]{GROUP, id}, locale));
    }

    private List<Teacher> getTeacherList(Long[] teacherIdList, Locale locale) throws EntityNotFoundException {
        return Arrays.stream(teacherIdList)
                .map(id -> teacherService.getById(id, locale))
                .collect(Collectors.toList());
    }
}