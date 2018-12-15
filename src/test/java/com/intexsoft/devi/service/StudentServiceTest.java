package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.repository.StudentsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * @author DEVIAPHAN
 * Test for Business Logic Service Class
 */
@RunWith(MockitoJUnitRunner.Silent.class)
@WebAppConfiguration
public class StudentServiceTest {

    @InjectMocks
    StudentService studentService;

    @Mock
    GroupService groupService;

    @Mock
    StudentsRepository studentsRepository;

    @Mock
    MessageSource messageSource;

    /**
     * Will return the list if all parameters are correct
     */
    @Test
    public void getAll() {
        List<Student> studentList = initializeStudentList();
        when(studentsRepository.findAll())
                .thenReturn(studentList);
        assertSame(studentList, studentService.getAll(Locale.ENGLISH));
    }

    /**
     * Will return an empty list if all parameters are correct
     */
    @Test
    public void getAll_NotFoundStudents() {
        when(studentsRepository.findAll())
                .thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(),studentService.getAll(Locale.ENGLISH));
    }

    /**
     * Will return a record by ID if all parameters are correct
     */
    @Test
    public void getById() {
        Student student = initializeStudent((long) 1);
        when(studentsRepository.findById((long) 1))
                .thenReturn(Optional.ofNullable(student));
        assertSame(student, studentService.getById((long) 1, Locale.ENGLISH));
    }

    /**
     * Will thrown out an exception if record by ID cannot found
     */
    @Test(expected = EntityNotFoundException.class)
    public void getById_NotFoundTeacher() {
        when(studentsRepository.findById((long) 2))
                .thenThrow(EntityNotFoundException.class);
        studentService.getById((long) 2, Locale.ENGLISH);
    }

    /**
     * Will thrown out an exception if ID will null
     */
    @Test(expected = EntityNotFoundException.class)
    public void getById_NullStudent() {
        when(studentsRepository.findById(null))
                .thenThrow(EntityNotFoundException.class);
        studentService.getById(null, Locale.ENGLISH);
    }

    /**
     * Will save a record if all parameters are correct
     */
    @Test
    public void save() {
        Student student = initializeStudent((long) 1);
        when(groupService.getById(null, Locale.ENGLISH))
                .thenReturn(null);
        when(studentsRepository.save(student))
                .thenReturn(student);
        assertSame(student, studentService.save(student, null, Locale.ENGLISH));
    }

    /**
     * Will thrown out an exception if ID will null
     */
    @Test(expected = EntityNotFoundException.class)
    public void save_NullGroupId() {
        Student student = initializeStudent((long) 1);
        when(groupService.getById(null, Locale.ENGLISH))
                .thenThrow(EntityNotFoundException.class);
        studentService.save(student, null, Locale.ENGLISH);
    }

    /**
     * Will thrown out an exception if ID will null
     */
    @Test(expected = NullPointerException.class)
    public void save_NullStudent() {
        when(studentsRepository.save(null))
                .thenThrow(NullPointerException.class);
        studentService.save(null, (long) 1, Locale.ENGLISH);
    }

    /**
     * Will return a record if all parameters are correct
     */
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

    /**
     * Will thrown out an exception if ID will null
     */
    @Test(expected = EntityNotFoundException.class)
    public void updateById_NullGroupId() {
        when(groupService.getById(null, Locale.ENGLISH))
                .thenThrow(EntityNotFoundException.class);
        studentService.updateById(initializeStudent((long) 1), (long) 2, null, Locale.ENGLISH);
    }

    /**
     * Will thrown out an exception if ALL ID will null
     */
    @Test(expected = EntityNotFoundException.class)
    public void updateById_NullAll() {
        when(studentsRepository.findById(null))
                .thenThrow(EntityNotFoundException.class);
        when(studentsRepository.save(null))
                .thenThrow(EntityNotFoundException.class);
        studentService.updateById(null, null, null, Locale.ENGLISH);
    }

    /**
     * Will thrown out an exception if ID will null
     */
    @Test(expected = EntityNotFoundException.class)
    public void deleteById_Null() {
        doThrow(EntityNotFoundException.class)
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