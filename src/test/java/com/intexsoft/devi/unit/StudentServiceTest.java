package com.intexsoft.devi.unit;

import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.repository.StudentRepository;
import com.intexsoft.devi.service.GroupServiceImpl;
import com.intexsoft.devi.service.StudentServiceImpl;
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
import static org.mockito.Mockito.when;

/**
 * @author DEVIAPHAN
 * Test for Business Logic Service Class
 */
@RunWith(MockitoJUnitRunner.Silent.class)
@WebAppConfiguration
public class StudentServiceTest {

    @InjectMocks
    StudentServiceImpl studentService;

    @Mock
    GroupServiceImpl groupService;

    @Mock
    StudentRepository studentRepository;

    @Mock
    MessageSource messageSource;

    /**
     * Will return the list if all parameters are correct
     */
    @Test
    public void getAll() {
        List<Student> studentList = initializeStudentList();
        when(studentRepository.findAll())
                .thenReturn(studentList);
        assertSame(studentList, studentService.getAll(Locale.ENGLISH));
    }

    /**
     * Will return an empty list if all parameters are correct
     */
    @Test
    public void getAll_NotFoundStudents() {
        when(studentRepository.findAll())
                .thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), studentService.getAll(Locale.ENGLISH));
    }

    /**
     * Will return a record by ID if all parameters are correct
     */
    @Test
    public void getById() {
        Student student = initializeStudent((long) 1);
        when(studentRepository.findById((long) 1))
                .thenReturn(Optional.ofNullable(student));
        assertSame(student, studentService.getById((long) 1, Locale.ENGLISH));
    }

    /**
     * Will thrown out an exception if record by ID cannot found
     */
    @Test(expected = EntityNotFoundException.class)
    public void getById_NotFoundTeacher() {
        when(studentRepository.findById((long) 2))
                .thenThrow(EntityNotFoundException.class);
        studentService.getById((long) 2, Locale.ENGLISH);
    }

    /**
     * Will return a list of records by group id
     */
    @Test
    public void getStudentsOfGroupById() {
        List<Student> studentList = initializeStudentList();
        when(studentRepository.findAllByGroup_Id((long) 1))
                .thenReturn(studentList);
        assertSame(studentList, studentService.getStudentsOfGroupById((long) 1, Locale.ENGLISH));
    }

    /**
     * Will return a record by name
     */
    @Test
    public void getByName() {
        Optional<Student> studentOptional = Optional.of(initializeStudent((long) 1));
        when(studentRepository.findByFirstNameAndLastName("First", "Last"))
                .thenReturn(studentOptional);
        assertSame(studentOptional, studentService.getByName("First", "Last"));
    }

    /**
     * Will save a record if all parameters are correct
     */
    @Test
    public void save() {
        Student student = initializeStudent((long) 1);
        when(groupService.getById(null, Locale.ENGLISH))
                .thenReturn(null);
        when(studentRepository.save(student))
                .thenReturn(student);
        assertSame(student, studentService.save(student, null, Locale.ENGLISH));
    }

    /**
     * Will return boolean value depending exist entity or not
     */
    @Test
    public void isStudentGroupExist() {
        Student student = initializeStudent((long) 1);
        when(studentRepository.findByFirstNameAndLastNameAndGroup_Number("first", "last", "group"))
                .thenReturn(Optional.ofNullable(student));
        assertTrue(studentService.isStudentGroupExist("first", "last", "group"));
    }

    /**
     * Will return a record if all parameters are correct
     */
    @Test
    public void updateById() {
        Student student = initializeStudent((long) 1);
        when(studentRepository.findById((long) 1))
                .thenReturn(Optional.ofNullable(student));
        when(groupService.getById(null, Locale.ENGLISH))
                .thenReturn(null);
        when(studentRepository.save(student))
                .thenReturn(student);
        assertSame(student, studentService.updateById(student, (long) 1, null, Locale.ENGLISH));
    }

    /**
     * Will thrown out an exception if ID will null
     */
    @Test(expected = EntityNotFoundException.class)
    public void updateById_NotFoundId() {
        when(studentRepository.findById((long) 2))
                .thenThrow(EntityNotFoundException.class);
        studentService.updateById(initializeStudent((long) 1), (long) 2, null, Locale.ENGLISH);
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