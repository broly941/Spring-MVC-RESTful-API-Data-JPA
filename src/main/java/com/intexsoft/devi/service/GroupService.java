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
        LOGGER.info(messageSource.getMessage("getAll", new Object[]{"groups"}, locale));
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
            LOGGER.info(messageSource.getMessage("getById", new Object[]{"group", id}, locale));
            return groupOptional.get();
        }
        LOGGER.error(messageSource.getMessage("EntityNotFoundException", new Object[]{"Get group by id", id}, locale));
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
        LOGGER.info(messageSource.getMessage("add", new Object[]{"group"}, locale));
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
            LOGGER.error(messageSource.getMessage("EntityNotFoundException", new Object[]{"Update group by id", groupId}, locale));
            throw new EntityNotFoundException();

        }
        Group currentGroup = groupOptional.get();
        currentGroup.setNumber(group.getNumber());
        currentGroup.setTeacher(teacherService.getById(curatorId, locale));
        currentGroup.setTeachers(getTeacherList(teacherIdList, locale));
        LOGGER.info(messageSource.getMessage("updateById", new Object[]{"group", groupId}, locale));
        return groupRepository.save(currentGroup);
    }

    /**
     * @param locale of messages
     * @param id     the group entity to be removed from the database
     */
    public void deleteById(Long id, Locale locale) {
        LOGGER.info(messageSource.getMessage("deletedById", new Object[]{"group", id}, locale));
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
