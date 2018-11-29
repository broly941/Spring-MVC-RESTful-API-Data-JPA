package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.exception.EntityNotFoundException;
import com.intexsoft.devi.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * @return all group entities in the database.
     */
    public List<Group> all() {
        return groupRepository.findAll();
    }

    /**
     * @param id
     * @return group entity by ID in the database.
     * @throws Exception if there is no value
     */
    public Group get(Long id) throws Exception {
        Optional<Group> groupOptional = groupRepository.findById(id);
        if (groupOptional.isPresent()) {
            return groupOptional.get();
        }
        throw new EntityNotFoundException(Group.class, "groupId", id.toString());
    }

    /**
     * @param group
     * @param curatorId
     * @param teacherIdList
     * @return added group entity in the database.
     * @throws Exception if there is no value
     */
    @Transactional
    public Group add(Group group, Long curatorId, Long[] teacherIdList) throws Exception {
        group.setTeacher(teacherService.get(curatorId));
        group.setTeachers(getTeacherList(teacherIdList));
        return groupRepository.save(group);
    }

    /**
     * @param group
     * @param groupId
     * @param curatorId
     * @param teacherIdList
     * @return updated group entity in the database.
     * @throws Exception if there is no value
     */
    @Transactional
    public Group update(Group group, Long groupId, Long curatorId, Long[] teacherIdList) throws Exception {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (!groupOptional.isPresent()) {
            throw new EntityNotFoundException(Group.class, "groupId", groupId.toString());
        }
        Group currentGroup = groupOptional.get();
        currentGroup.setNumber(group.getNumber());
        currentGroup.setTeacher(teacherService.get(curatorId));
        currentGroup.setTeachers(getTeacherList(teacherIdList));
        return groupRepository.save(currentGroup);
    }

    /**
     * @param id the group entity to be removed from the database
     */
    public void delete(Long id) {
        groupRepository.deleteById(id);
    }

    private List<Teacher> getTeacherList(Long[] teacherIdList) throws Exception {
        List<Teacher> teacherList = new ArrayList<>();
        for (Long id : teacherIdList) {
            teacherList.add(teacherService.get(id));
        }
        return teacherList;
    }
}
