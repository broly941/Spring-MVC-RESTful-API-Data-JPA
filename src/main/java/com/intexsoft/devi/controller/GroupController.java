package com.intexsoft.devi.controller;

import com.intexsoft.devi.dto.GroupDTO;
import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.service.GroupService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author DEVIAPHAN
 * Processes the request and returns the response as JSON.
 */
@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    GroupService groupService;

    @Autowired
    ModelMapper modelMapper;

    /**
     * @return getAll group entities in the database.
     */
    @GetMapping
    public List<GroupDTO> getAll(Locale locale) {
        return groupService.getAll(locale).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * @param id of group
     * @return group entity by ID in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @GetMapping("/{id}")
    public GroupDTO getById(@PathVariable Long id, Locale locale) throws EntityNotFoundException {
        return convertToDto(groupService.getById(id, locale));
    }

    /**
     *
     * @param id
     * @param locale
     * @return
     * @throws EntityNotFoundException
     */
    @GetMapping("/getByTeacherId/{id}")
    public List<GroupDTO> getGroupsOfTeacherById(@PathVariable Long id, Locale locale) throws EntityNotFoundException {
        return groupService.getGroupsOfTeacherById(id, locale).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * @param groupDTO      entity
     * @param curatorId     of teacher
     * @param teacherIdList consist of teachers
     * @return added group entity in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @PostMapping
    public GroupDTO save(@RequestBody GroupDTO groupDTO, @RequestParam Long curatorId, @RequestParam Long[] teacherIdList, Locale locale) throws EntityNotFoundException {
        Group group = groupService.save(convertToEntity(groupDTO), curatorId, teacherIdList, locale);
        return convertToDto(group);
    }

    /**
     * @param groupDTO      entity
     * @param groupId       of group
     * @param curatorId     of teacher
     * @param teacherIdList consist of teachers
     * @return updated group entity in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @PutMapping("/{groupId}")
    public GroupDTO updateById(@RequestBody GroupDTO groupDTO, @PathVariable Long groupId, @RequestParam Long curatorId, @RequestParam Long[] teacherIdList, Locale locale) throws EntityNotFoundException {
        Group group = groupService.updateById(convertToEntity(groupDTO), groupId, curatorId, teacherIdList, locale);
        return convertToDto(group);
    }

    /**
     * @param id the group entity to be removed from the database
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id, Locale locale) {
        groupService.deleteById(id, locale);
    }

    private GroupDTO convertToDto(Group group) {
        return modelMapper.map(group, GroupDTO.class);
    }

    private Group convertToEntity(GroupDTO groupDTO) {
        return modelMapper.map(groupDTO, Group.class);
    }
}