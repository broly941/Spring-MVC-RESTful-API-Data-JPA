package com.intexsoft.devi.controller;

import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

/**
 * @author DEVIAPHAN
 * Processes the request and returns the response as JSON.
 */
@RestController
@RequestMapping("/university/students")
public class StudentController {
    @Autowired
    StudentService studentService;

    /**
     * @return all student entities in the database.
     */
    @GetMapping("")
    public List<Student> all(@RequestHeader("Accept-Language") Locale locale) {
        return studentService.all(locale);
    }

    /**
     * @param id
     * @return student entity by ID in the database.
     */
    @GetMapping("/{id}")
    public Student get(@PathVariable Long id, @RequestHeader("Accept-Language") Locale locale) throws Exception {
        return studentService.get(id, locale);
    }

    /**
     * @param student
     * @param groupId
     * @return added student entity in the database.
     * @throws Exception if there is no value
     */
    @PostMapping("")
    public Student add(@RequestBody Student student, @RequestParam Long groupId, @RequestHeader("Accept-Language") Locale locale) throws Exception {
        return studentService.add(student, groupId, locale);
    }

    /**
     * @param student
     * @param studentId
     * @param groupId
     * @return updated student entity in the database.
     * @throws Exception if there is no value
     */
    @PutMapping("/{studentId}")
    public Student update(@RequestBody Student student, @PathVariable Long studentId, @RequestParam Long groupId, @RequestHeader("Accept-Language") Locale locale) throws Exception {
        return studentService.update(student, studentId, groupId, locale);
    }

    /**
     * @param id the student entity to be removed from the database
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @RequestHeader("Accept-Language") Locale locale) {
        studentService.delete(id, locale);
    }
}
