package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.repository.StudentsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Min;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
@WebAppConfiguration
public class StudentServiceTest {

    @InjectMocks
    StudentService studentService;

    @Mock
    GroupService groupService;

    @Mock
    StudentsRepository studentsRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAll() {
        List<Student> studentList = initializeStudentList();
        when(studentsRepository.findAll())
                .thenReturn(studentList);
        assertSame(studentList, studentService.getAll(Locale.ENGLISH));
    }

    @Test
    public void getAll_Null() {
        when(studentsRepository.findAll())
                .thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(),studentService.getAll(Locale.ENGLISH));
    }

    @Test
    public void getById() {
        Student student = initializeStudent((long) 1);
        when(studentsRepository.findById((long) 1))
                .thenReturn(Optional.ofNullable(student));
        assertSame(student, studentService.getById((long) 1, Locale.ENGLISH));
    }

    @Test(expected = EntityNotFoundException.class)
    public void getById_NotFoundId() {
        when(studentsRepository.findById((long) 2))
                .thenThrow(EntityNotFoundException.class);
        studentService.getById((long) 2, Locale.ENGLISH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getById_Null() {
        when(studentsRepository.findById(null))
                .thenThrow(IllegalArgumentException.class);
        studentService.getById(null, Locale.ENGLISH);
    }

    @Test
    public void save() {
        Student student = initializeStudent((long) 1);
        when(groupService.getById(null, Locale.ENGLISH))
                .thenReturn(null);
        when(studentsRepository.save(student))
                .thenReturn(student);
        assertSame(student, studentService.save(student, null, Locale.ENGLISH));
    }

    @Test(expected = IllegalArgumentException.class)
    public void save_Null() {
        when(groupService.getById(null, Locale.ENGLISH))
                .thenThrow(IllegalArgumentException.class);
        when(studentsRepository.save(null))
                .thenThrow(IllegalArgumentException.class);
        studentService.save(null, null, Locale.ENGLISH);
    }

    @Test
    public void updateById() {
        Student student = initializeStudent((long) 1);
        when(studentsRepository.findById((long) 1))
                .thenReturn(Optional.ofNullable(student));
        when(groupService.getById(null, Locale.ENGLISH))
                .thenReturn(null);
        when(studentsRepository.save(student))
                .thenReturn(student);
        assertSame(student, studentService.updateById(student, (long) 1, null, Locale.ENGLISH));
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateById_NotFoundId() {
        when(studentsRepository.findById((long) 2))
                .thenThrow(EntityNotFoundException.class);
        studentService.updateById(initializeStudent((long) 1), (long) 2, null, Locale.ENGLISH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateById_Null() {
        when(studentsRepository.findById(null))
                .thenThrow(IllegalArgumentException.class);
        when(studentsRepository.save(null))
                .thenThrow(IllegalArgumentException.class);
        studentService.updateById(null, null, null, Locale.ENGLISH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteById_Null() {
        doThrow(IllegalArgumentException.class)
                .when(studentsRepository).deleteById(null);
        studentService.deleteById(null, Locale.ENGLISH);
    }

    private List<Student> initializeStudentList() {
        List<Student> studentList = new ArrayList<>();
        studentList.add(initializeStudent((long) 1));
        studentList.add(initializeStudent((long) 2));
        return studentList;
    }

    private Student initializeStudent(long id) {
        Student student = new Student();
        student.setFirstName("First");
        student.setLastName("Last");
        student.setId(id);

        return student;
    }
}