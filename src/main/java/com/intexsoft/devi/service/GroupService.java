package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.repository.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author DEVIAPHAN
 * Business Logic Service Class
 */
@Service
public class GroupService {

    private static final String ENTITY_NOT_FOUND_EXCEPTION = "EntityNotFoundException";
    private static final String GROUP = "group";
    private static final String GROUPS = "groups";
    private static final String UPDATE_GROUP_BY_ID = "Update group by id";
    private static final String GET_GROUP_BY_ID = "Get group by id";
    private static final String GET_BY_ID = "getById";
    private static final String GET_ALL = "getAll";
    private static final String ADD = "add";
    private static final String UPDATE_BY_ID = "updateById";
    private static final String DELETED_BY_ID = "deletedById";


    @Autowired
    GroupRepository groupRepository;

    @Autowired
    TeacherService teacherService;

    @Autowired
    MessageSource messageSource;

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GroupService.class);

    /**
     * @param locale of messages
     * @return getAll group entities in the database.
     */
    public List<Group> getAll(Locale locale) {
        LOGGER.info(messageSource.getMessage(GET_ALL, new Object[]{GROUPS}, locale));
        return groupRepository.findAll();
    }

    /**
     * @param id     of group
     * @param locale of messages
     * @return group entity by ID in the database.
     * @throws EntityNotFoundException if there is no value
     */
    public Group getById(Long id, Locale locale) throws EntityNotFoundException {
        Optional<Group> groupOptional = groupRepository.findById(id);
        if (groupOptional.isPresent()) {
            LOGGER.info(messageSource.getMessage(GET_BY_ID, new Object[]{GROUP, id}, locale));
            return groupOptional.get();
        }
        LOGGER.error(messageSource.getMessage(ENTITY_NOT_FOUND_EXCEPTION, new Object[]{GET_GROUP_BY_ID, id}, locale));
        throw new EntityNotFoundException();
    }

    /**
     * @param group         entity
     * @param curatorId     of teacher
     * @param teacherIdList consist of teachers
     * @param locale        of messages
     * @return added group entity in the database.
     */
    @Transactional
    public Group save(Group group, Long curatorId, Long[] teacherIdList, Locale locale) {
        group.setTeacher(teacherService.getById(curatorId, locale));
        group.setTeachers(getTeacherList(teacherIdList, locale));
        LOGGER.info(messageSource.getMessage(ADD, new Object[]{GROUP}, locale));
        return groupRepository.save(group);
    }

    /**
     * @param group         entity
     * @param groupId       of group
     * @param curatorId     of teacher
     * @param teacherIdList consist of teachers
     * @param locale        of messages
     * @return updated group entity in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @Transactional
    public Group updateById(Group group, Long groupId, Long curatorId, Long[] teacherIdList, Locale locale) throws EntityNotFoundException {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (!groupOptional.isPresent()) {
            LOGGER.error(messageSource.getMessage(ENTITY_NOT_FOUND_EXCEPTION, new Object[]{UPDATE_GROUP_BY_ID, groupId}, locale));
            throw new EntityNotFoundException();

        }
        Group currentGroup = groupOptional.get();
        currentGroup.setNumber(group.getNumber());
        currentGroup.setTeacher(teacherService.getById(curatorId, locale));
        currentGroup.setTeachers(getTeacherList(teacherIdList, locale));
        LOGGER.info(messageSource.getMessage(UPDATE_BY_ID, new Object[]{GROUP, groupId}, locale));
        return groupRepository.save(currentGroup);
    }

    /**
     * @param locale of messages
     * @param id     the group entity to be removed from the database
     */
    public void deleteById(Long id, Locale locale) {
        LOGGER.info(messageSource.getMessage(DELETED_BY_ID, new Object[]{GROUP, id}, locale));
        groupRepository.deleteById(id);
    }

    private List<Teacher> getTeacherList(Long[] teacherIdList, Locale locale) throws EntityNotFoundException {
        List<Teacher> teacherList = new ArrayList<>();
        for (Long id : teacherIdList) {
            teacherList.add(teacherService.getById(id, locale));
        }
        return teacherList;
    }
}
