package com.intexsoft.devi.service;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.repository.GroupRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Will thrown out an exception if ID will null
     */
    @Test(expected = EntityNotFoundException.class)
    public void getById_NullGroup() {
        when(groupRepository.findById(null))
                .thenThrow(EntityNotFoundException.class);
        groupService.getById(null, Locale.ENGLISH);
    }

    /**
     * Will save a record if all parameters are correct
     */
    @Test
    public void save() {
        Group group = initializeGroup((long) 1);
        when(groupRepository.save(group))
                .thenReturn(group);
        assertSame(group, groupService.save(group, (long) 2, new Long[]{}, Locale.ENGLISH));
    }

    /**
     * Will thrown out an exception if entity will null
     */
    @Test(expected = NullPointerException.class)
    public void save_NullGroup() {
        when(groupRepository.save(null))
                .thenThrow(NullPointerException.class);
        groupService.save(null, (long) 1, new Long[]{}, Locale.ENGLISH);
    }

    /**
     * Will thrown out an exception if ID will null
     */
    @Test(expected = EntityNotFoundException.class)
    public void save_NullCuratorId() {
        Group group = initializeGroup((long) 1);
        when(teacherService.getById(null, Locale.ENGLISH))
                .thenThrow(EntityNotFoundException.class);
        //ALL WHEN
        groupService.save(group, null, new Long[]{}, Locale.ENGLISH);
    }

    /**
     * Will thrown out an exception if ID will null
     */
    @Test(expected = NullPointerException.class)
    public void save_NullTeacherIdList() {
        Group group = initializeGroup((long) 1);
        when(teacherService.getById(null, Locale.ENGLISH))
                .thenThrow(NullPointerException.class);
        groupService.save(group, (long) 1, null, Locale.ENGLISH);
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
    /**
     * Will thrown out an exception if ALL ID will null
     */
    @Test(expected = EntityNotFoundException.class)
    public void updateById_NullAll() {
        when(groupRepository.findById(null))
                .thenThrow(EntityNotFoundException.class);
        when(teacherService.getById(null, Locale.ENGLISH))
                .thenThrow(EntityNotFoundException.class);
        when(groupRepository.save(null))
                .thenThrow(EntityNotFoundException.class);
        groupService.updateById(null, null, null, null, Locale.ENGLISH);
    }

    /**
     * Will thrown out an exception if ID will null
     */
    @Test(expected = EntityNotFoundException.class)
    public void deleteById_Null() {
        doThrow(EntityNotFoundException.class)
                .when(groupRepository).deleteById(null);
        groupService.deleteById(null, Locale.ENGLISH);
    }

    private List<Group> initializeGroupList() {
        List<Group> groupList = new ArrayList<>();
        groupList.add(initializeGroup((long) 1));
        groupList.add(initializeGroup((long) 2));
        return groupList;
    }

    private Group initializeGroup(long id) {
        Group group = new Group();
        group.setNumber("ПОИТ-" + id);
        group.setId(id);

        return group;
    }
}