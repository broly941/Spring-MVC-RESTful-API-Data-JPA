package com.intexsoft.devi.unit;

import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.repository.TeacherRepository;
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
import static org.mockito.Mockito.when;

/**
 * @author DEVIAPHAN
 * Test for Business Logic Service Class
 */
@RunWith(MockitoJUnitRunner.Silent.class)
@WebAppConfiguration
public class TeacherServiceTest {

    @InjectMocks
    TeacherService teacherService;

    @Mock
    TeacherRepository teacherRepository;

    @Mock
    MessageSource messageSource;

    /**
     * Will return the list if all parameters are correct
     */
    @Test
    public void getAll() {
        List<Teacher> teacherList = initializeTeacherList();
        when(teacherRepository.findAll())
                .thenReturn(teacherList);
        assertSame(teacherList, teacherService.getAll(Locale.ENGLISH));
    }

    /**
     * Will return an empty list if all parameters are correct
     */
    @Test
    public void getAll_NotFoundTeachers() {
        when(teacherRepository.findAll())
                .thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), teacherService.getAll(Locale.ENGLISH));
    }

    /**
     * Will return a record by ID if all parameters are correct
     */
    @Test
    public void getById() {
        Teacher teacher = initializeTeacher((long) 1);
        when(teacherRepository.findById((long) 1))
                .thenReturn(Optional.ofNullable(teacher));
        assertSame(teacher, teacherService.getById((long) 1, Locale.ENGLISH));
    }

    /**
     * Will thrown out an exception if record by ID cannot found
     */
    @Test(expected = EntityNotFoundException.class)
    public void getById_NotFoundTeacher() {
        when(teacherRepository.findById((long) 2))
                .thenThrow(EntityNotFoundException.class);
        teacherService.getById((long) 2, Locale.ENGLISH);
    }

    /**
     * Will save a record if all parameters are correct
     */
    @Test
    public void save() {
        Teacher teacher = initializeTeacher((long) 1);
        when(teacherRepository.save(teacher))
                .thenReturn(teacher);
        assertSame(teacher, teacherService.save(teacher, Locale.ENGLISH));
    }

    /**
     * Will return a record if all parameters are correct
     */
    @Test
    public void updateById() {
        Teacher teacher = initializeTeacher((long) 1);
        when(teacherRepository.findById((long) 1))
                .thenReturn(Optional.ofNullable(teacher));
        when(teacherRepository.save(teacher))
                .thenReturn(teacher);
        assertSame(teacher, teacherService.updateById(teacher, (long) 1, Locale.ENGLISH));
    }

    /**
     * Will thrown out an exception if entity cannot found
     */
    @Test(expected = EntityNotFoundException.class)
    public void updateById_NotFoundId() {
        when(teacherRepository.findById((long) 2))
                .thenThrow(EntityNotFoundException.class);
        teacherService.updateById(initializeTeacher((long) 1), (long) 1, Locale.ENGLISH);
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