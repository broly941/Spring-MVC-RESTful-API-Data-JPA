package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.repository.TeacherRepository;
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
public class TeacherServiceTest {

    @InjectMocks
    TeacherService teacherService;

    @Mock
    TeacherRepository teacherRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAll() {
        List<Teacher> teacherList = initializeTeacherList();
        when(teacherRepository.findAll())
                .thenReturn(teacherList);
        assertSame(teacherList, teacherService.getAll(Locale.ENGLISH));
    }

    @Test
    public void getAll_Null() {
        when(teacherRepository.findAll())
                .thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(),teacherService.getAll(Locale.ENGLISH));
    }

    @Test
    public void getById() {
        Teacher teacher = initializeTeacher((long) 1);
        when(teacherRepository.findById((long) 1))
                .thenReturn(Optional.ofNullable(teacher));
        assertSame(teacher, teacherService.getById((long) 1, Locale.ENGLISH));
    }

    @Test(expected = EntityNotFoundException.class)
    public void getById_NotFoundId() {
        when(teacherRepository.findById((long) 2))
                .thenThrow(EntityNotFoundException.class);
        teacherService.getById((long) 2, Locale.ENGLISH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getById_Null() {
        when(teacherRepository.findById(null))
                .thenThrow(IllegalArgumentException.class);
        teacherService.getById(null, Locale.ENGLISH);
    }

    @Test
    public void save() {
        Teacher teacher = initializeTeacher((long) 1);
        when(teacherRepository.save(teacher))
                .thenReturn(teacher);
        assertSame(teacher, teacherService.save(teacher, Locale.ENGLISH));
    }

    @Test(expected = IllegalArgumentException.class)
    public void save_Null() {
        when(teacherRepository.save(null))
                .thenThrow(IllegalArgumentException.class);
        teacherService.save(null, Locale.ENGLISH);
    }

    @Test
    public void updateById() {
        Teacher teacher = initializeTeacher((long) 1);
        when(teacherRepository.findById((long) 1))
                .thenReturn(Optional.ofNullable(teacher));
        when(teacherRepository.save(teacher))
                .thenReturn(teacher);
        assertSame(teacher, teacherService.updateById(teacher, (long) 1, Locale.ENGLISH));
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateById_NotFoundId() {
        when(teacherRepository.findById((long) 2))
                .thenThrow(EntityNotFoundException.class);
        teacherService.updateById(initializeTeacher((long) 1), (long) 1, Locale.ENGLISH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateById_Null() {
        when(teacherRepository.findById(null))
                .thenThrow(IllegalArgumentException.class);
        when(teacherRepository.save(null))
                .thenThrow(IllegalArgumentException.class);
        teacherService.updateById(null, null, Locale.ENGLISH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteById_Null() {
        doThrow(IllegalArgumentException.class)
                .when(teacherRepository).deleteById(null);
        teacherService.deleteById(null, Locale.ENGLISH);
    }

    private List<Teacher> initializeTeacherList() {
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(initializeTeacher((long) 1));
        teacherList.add(initializeTeacher((long) 2));
        return teacherList;
    }

    private Teacher initializeTeacher(long id) {
        Teacher teacher = new Teacher();
        teacher.setFirstName("First");
        teacher.setLastName("Last");
        teacher.setId(id);

        return teacher;
    }
}