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
     * @param locale
     * @return all group entities in the database.
     */
    public List<Group> all(Locale locale) {
        LOGGER.info(messageSource.getMessage("getAll", new Object[]{"groups"}, locale));
        return groupRepository.findAll();
    }

    /**
     * @param id
     * @param locale
     * @return group entity by ID in the database.
     * @throws EntityNotFoundException if there is no value
     */
    public Group get(Long id, Locale locale) throws EntityNotFoundException {
        Optional<Group> groupOptional = groupRepository.findById(id);
        if (groupOptional.isPresent()) {
            LOGGER.info(messageSource.getMessage("getById", new Object[]{"group", id}, locale));
            return groupOptional.get();
        }
        LOGGER.error(messageSource.getMessage("EntityNotFoundException", new Object[]{"Get group by id", id}, locale));
        throw new EntityNotFoundException();
    }

    /**
     * @param group
     * @param curatorId
     * @param teacherIdList
     * @param locale
     * @return added group entity in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @Transactional
    public Group add(Group group, Long curatorId, Long[] teacherIdList, Locale locale) throws EntityNotFoundException {
        group.setTeacher(teacherService.get(curatorId, locale));
        group.setTeachers(getTeacherList(teacherIdList, locale));
        LOGGER.info(messageSource.getMessage("add", new Object[]{"group"}, locale));
        return groupRepository.save(group);
    }

    /**
     * @param group
     * @param groupId
     * @param curatorId
     * @param teacherIdList
     * @param locale
     * @return updated group entity in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @Transactional
    public Group update(Group group, Long groupId, Long curatorId, Long[] teacherIdList, Locale locale) throws EntityNotFoundException {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (!groupOptional.isPresent()) {
            LOGGER.error(messageSource.getMessage("EntityNotFoundException", new Object[]{"Update group by id", groupId}, locale));
            throw new EntityNotFoundException();

        }
        Group currentGroup = groupOptional.get();
        currentGroup.setNumber(group.getNumber());
        currentGroup.setTeacher(teacherService.get(curatorId, locale));
        currentGroup.setTeachers(getTeacherList(teacherIdList, locale));
        LOGGER.info(messageSource.getMessage("updateById", new Object[]{"group", groupId}, locale));
        return groupRepository.save(currentGroup);
    }

    /**
     * @param locale
     * @param id the group entity to be removed from the database
     */
    public void delete(Long id, Locale locale) {
        LOGGER.info(messageSource.getMessage("deletedById", new Object[]{"group", id}, locale));
        groupRepository.deleteById(id);
    }

    private List<Teacher> getTeacherList(Long[] teacherIdList, Locale locale) throws EntityNotFoundException {
        List<Teacher> teacherList = new ArrayList<>();
        for (Long id : teacherIdList) {
            teacherList.add(teacherService.get(id, locale));
        }
        return teacherList;
    }
}
