package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.repository.GroupRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
@WebAppConfiguration
public class GroupServiceTest {

    @InjectMocks
    GroupService groupService;

    @Mock
    GroupRepository groupRepository;

    @Mock
    TeacherService teacherService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAll() {
        List<Group> groupList = initializeGroupList();
        when(groupRepository.findAll())
                .thenReturn(groupList);
        assertSame(groupList, groupService.getAll(Locale.ENGLISH));
    }

    @Test
    public void getAll_Null() {
        when(groupRepository.findAll())
                .thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(),groupService.getAll(Locale.ENGLISH));
    }

    @Test
    public void getById() {
        Group group = initializeGroup((long) 1);
        when(groupRepository.findById((long) 1))
                .thenReturn(Optional.ofNullable(group));
        assertSame(group, groupService.getById((long) 1, Locale.ENGLISH));
    }

    @Test(expected = EntityNotFoundException.class)
    public void getById_NotFoundId() {
        when(groupRepository.findById((long) 2))
                .thenThrow(EntityNotFoundException.class);
        groupService.getById((long) 2, Locale.ENGLISH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getById_Null() {
        when(groupRepository.findById(null))
                .thenThrow(IllegalArgumentException.class);
        groupService.getById(null, Locale.ENGLISH);
    }

    @Test
    public void save() {
        Group group = initializeGroup((long) 1);
        when(teacherService.getById(null, Locale.ENGLISH))
                .thenReturn(null);
        when(groupRepository.save(group))
                .thenReturn(group);
        assertSame(group, groupService.save(group, null, new Long[]{}, Locale.ENGLISH));
    }

    @Test(expected = IllegalArgumentException.class)
    public void save_Null() {
        Group group = initializeGroup((long) 1);
        when(teacherService.getById(null, Locale.ENGLISH))
                .thenThrow(IllegalArgumentException.class);
        when(groupRepository.save(null))
                .thenThrow(IllegalArgumentException.class);
       groupService.save(group, null, null, Locale.ENGLISH);
    }

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

    @Test(expected = EntityNotFoundException.class)
    public void updateById_NotFoundId() {
        when(groupRepository.findById((long) 2))
                .thenThrow(EntityNotFoundException.class);
        groupService.updateById(initializeGroup((long) 1), (long) 1, null, new Long[]{}, Locale.ENGLISH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateById_Null() {
        when(groupRepository.findById(null))
                .thenThrow(IllegalArgumentException.class);
        when(teacherService.getById(null, Locale.ENGLISH))
                .thenThrow(IllegalArgumentException.class);
        when(groupRepository.save(null))
                .thenThrow(IllegalArgumentException.class);
       groupService.updateById(null, null, null, null, Locale.ENGLISH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteById_Null() {
        doThrow(IllegalArgumentException.class)
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