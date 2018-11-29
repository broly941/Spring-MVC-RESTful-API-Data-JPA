package com.intexsoft.devi.controller;

import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author DEVIAPHAN
 * Processes the request and returns the response as JSON.
 */
@RestController
@RequestMapping("/university/groups")
public class GroupController {
    @Autowired
    GroupService groupService;

    private static final Logger logger =
            LoggerFactory.getLogger(GroupController.class);

    /**
     * @return all group entities in the database.
     */
    @GetMapping("")
    public List<Group> listAllGroups() {
        logger.info("get all groups");
        return groupService.all();
    }

    /**
     * @param id
     * @return group entity by ID in the database.
     * @throws Exception if there is no value
     */
    @GetMapping("/{id}")
    public Group getGroup(@PathVariable Long id) throws Exception {
        logger.info("get group by id: " + id);
        return groupService.get(id);
    }

    /**
     * @param group
     * @param curatorId
     * @param teacherIdList
     * @return added group entity in the database.
     * @throws Exception if there is no value
     */
    @PostMapping("")
    public Group addGroup(@RequestBody Group group, @RequestParam Long curatorId, @RequestParam Long[] teacherIdList) throws Exception {
        logger.info("add group " + group.getNumber());
        return groupService.add(group, curatorId, teacherIdList);
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
    public Group updateGroup(@RequestBody Group group, @PathVariable Long groupId, @RequestParam Long curatorId, @RequestParam Long[] teacherIdList) throws Exception {
        logger.info("update group " + group.getNumber());
        return groupService.update(group, groupId, curatorId, teacherIdList);
    }

    /**
     * @param id the group entity to be removed from the database
     */
    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable Long id) {
        logger.info("delete group by id: " + id);
        groupService.delete(id);
    }
}
