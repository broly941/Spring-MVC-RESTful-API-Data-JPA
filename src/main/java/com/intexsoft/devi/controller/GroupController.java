package com.intexsoft.devi.controller;

import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Locale;

/**
 * @author DEVIAPHAN
 * Processes the request and returns the response as JSON.
 */
@RestController
@RequestMapping("/university/groups")
public class GroupController {
    @Autowired
    GroupService groupService;

    /**
     * @return getAll group entities in the database.
     */
    @GetMapping("")
    public List<Group> getAll(@RequestHeader("Accept-Language") Locale locale) {
        return groupService.getAll(locale);
    }

    /**
     * @param id
     * @return group entity by ID in the database.
     * @throws Exception if there is no value
     */
    @GetMapping("/{id}")
    public Group getById(@PathVariable Long id, @RequestHeader("Accept-Language") Locale locale) throws EntityNotFoundException {
        return groupService.getById(id, locale);
    }

    /**
     * @param group
     * @param curatorId
     * @param teacherIdList
     * @return added group entity in the database.
     * @throws Exception if there is no value
     */
    @PostMapping("")
    public Group save(@RequestBody Group group, @RequestParam Long curatorId, @RequestParam Long[] teacherIdList, @RequestHeader("Accept-Language") Locale locale) throws EntityNotFoundException {
        return groupService.save(group, curatorId, teacherIdList, locale);
    }

    /**
     * @param group
     * @param groupId
     * @param curatorId
     * @param teacherIdList
     * @return updated group entity in the database.
     * @throws Exception if there is no value
     */
    @PutMapping("/{groupId}")
    public Group updateById(@RequestBody Group group, @PathVariable Long groupId, @RequestParam Long curatorId, @RequestParam Long[] teacherIdList, @RequestHeader("Accept-Language") Locale locale) throws EntityNotFoundException {
        return groupService.updateById(group, groupId, curatorId, teacherIdList, locale);
    }

    /**
     * @param id the group entity to be removed from the database
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id, @RequestHeader("Accept-Language") Locale locale) {
        groupService.deleteById(id, locale);
    }
}
