package com.intexsoft.devi.controller;

import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Locale;

/**
 * @author DEVIAPHAN
 * Processes the request and returns the response as JSON.
 */
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    StudentService studentService;

    /**
     * @return getAll student entities in the database.
     */
    @GetMapping("")
    public List<Student> getAll(@RequestHeader(value = "Accept-language", defaultValue = "en") Locale locale) {
        return studentService.getAll(locale);
    }

    /**
     * @param id of student
     * @return student entity by ID in the database.
     */
    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id, @RequestHeader(value = "Accept-language", defaultValue = "en") Locale locale) throws EntityNotFoundException {
        return studentService.getById(id, locale);
    }

    /**
     * @param student entity
     * @param groupId of Group
     * @return added student entity in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @PostMapping("")
    public Student save(@RequestBody Student student, @RequestParam Long groupId, @RequestHeader(value = "Accept-language", defaultValue = "en") Locale locale) throws EntityNotFoundException {
        return studentService.save(student, groupId, locale);
    }

    /**
     * @param student   entity
     * @param studentId of student
     * @param groupId   of group
     * @return updated student entity in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @PutMapping("/{studentId}")
    public Student updateById(@RequestBody Student student, @PathVariable Long studentId, @RequestParam Long groupId, @RequestHeader(value = "Accept-language", defaultValue = "en") Locale locale) throws EntityNotFoundException {
        return studentService.updateById(student, studentId, groupId, locale);
    }

    /**
     * @param id the student entity to be removed from the database
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id, @RequestHeader(value = "Accept-language", defaultValue = "en") Locale locale) {
        studentService.deleteById(id, locale);
    }
}
