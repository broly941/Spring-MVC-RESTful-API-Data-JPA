package com.intexsoft.devi.controller;

import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author DEVIAPHAN
 * Processes the request and returns the response as JSON.
 */
@RestController
@RequestMapping("/university/students")
public class StudentController {
    @Autowired
    StudentService studentService;

    private static final Logger logger =
            LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private MessageSource messageSource;

    /**
     * @return all student entities in the database.
     */
    @GetMapping("")
    public List<Student> listAllStudents() {
        logger.info("get all students");
        return studentService.all();
    }

    /**
     * @param id
     * @return student entity by ID in the database.
     */
    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) throws Exception {
        logger.info("get student by id: " + id);
        return studentService.get(id);
    }

    /**
     * @param student
     * @param groupId
     * @return added student entity in the database.
     * @throws Exception if there is no value
     */
    @PostMapping("")
    public Student addStudents(@RequestBody Student student, @RequestParam Long groupId) throws Exception {
        logger.info("add student " + student.getFirstName() + " " + student.getLastName());
        return studentService.add(student, groupId);
    }

    /**
     * @param student
     * @param studentId
     * @param groupId
     * @return updated student entity in the database.
     * @throws Exception if there is no value
     */
    @PutMapping("/{studentId}")
    public Student updateStudent(@RequestBody Student student, @PathVariable Long studentId, @RequestParam Long groupId) throws Exception {
        logger.info("update student " + student.getFirstName() + " " + student.getLastName());
        return studentService.update(student, studentId, groupId);
    }

    /**
     * @param id the student entity to be removed from the database
     */
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        logger.info("delete student by id: " + id);
        studentService.delete(id);
    }
}
