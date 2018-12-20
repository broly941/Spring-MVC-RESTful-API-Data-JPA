package com.intexsoft.devi.unit;

import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.repository.GroupRepository;
import com.intexsoft.devi.service.GroupService;
import com.intexsoft.devi.service.TeacherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

/**
 * @author DEVIAPHAN
 * Test for Business Logic Service Class
 */
@RunWith(MockitoJUnitRunner.Silent.class)
@WebAppConfiguration
public class GroupServiceTest {
    @InjectMocks
    GroupService groupService;

    @Mock
    GroupRepository groupRepository;

    @Mock
    TeacherService teacherService;

    @Mock
    MessageSource messageSource;


    /**
     * Will return the list if all parameters are correct
     */
    @Test
    public void getAll() {
        List<Group> groupList = initializeGroupList();
        when(groupRepository.findAll())
                .thenReturn(groupList);
        assertSame(groupList, groupService.getAll(Locale.ENGLISH));
    }

    /**
     * Will return an empty list if all parameters are correct
     */
    @Test
    public void getAll_NotFoundGroups() {
        when(groupRepository.findAll())
                .thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), groupService.getAll(Locale.ENGLISH));
    }

    /**
     * Will return a record by ID if all parameters are correct
     */
    @Test
    public void getById() {
        Group group = initializeGroup((long) 1);
        when(groupRepository.findById((long) 1))
                .thenReturn(Optional.ofNullable(group));
        assertSame(group, groupService.getById((long) 1, Locale.ENGLISH));
    }

    /**
     * Will thrown out an exception if record by ID cannot found
     */
    @Test(expected = EntityNotFoundException.class)
    public void getById_NotFoundGroup() {
        when(groupRepository.findById((long) 2))
                .thenThrow(EntityNotFoundException.class);
        groupService.getById((long) 2, Locale.ENGLISH);
    }

    /**
     * Will save a record if all parameters are correct
     */
    @Test
    public void save() {
        Group group = initializeGroup((long) 1);
        when(teacherService.getById((long) 2, Locale.ENGLISH))
                .thenReturn(null);
        when(groupRepository.save(group))
                .thenReturn(group);
        assertSame(group, groupService.save(group, (long) 2, new Long[]{}, Locale.ENGLISH));
    }

    /**
     * Will return a record if all parameters are correct
     */
    @Test
    public void updateById() {
        Group group = initializeGroup((long) 1);
        when(groupRepository.findById((long) 1))
                .thenReturn(Optional.ofNullable(group));
        when(teacherService.getById(null, Locale.ENGLISH))
                .thenReturn(null);
        when(groupRepository.save(group))
                .thenReturn(group);
        assertSame(group, groupService.updateById(group, (long) 1, null, new Long[]{}, Locale.ENGLISH));
    }

    /**
     * Will thrown out an exception if entity cannot found
     */
    @Test(expected = EntityNotFoundException.class)
    public void updateById_NotFoundId() {
        when(groupRepository.findById((long) 2))
                .thenThrow(EntityNotFoundException.class);
        groupService.updateById(initializeGroup((long) 1), (long) 1, (long) 1, new Long[]{}, Locale.ENGLISH);
    }

    private List<Group> initializeGroupList() {
        List<Group> groupList = new ArrayList<>();
        groupList.add(initializeGroup((long) 1));
        groupList.add(initializeGroup((long) 2));
        return groupList;
    }

    private Group initializeGroup(long id) {
        Group group = new Group();
        group.setNumber("POIT-" + id);
        group.setId(id);

        return group;
    }
}