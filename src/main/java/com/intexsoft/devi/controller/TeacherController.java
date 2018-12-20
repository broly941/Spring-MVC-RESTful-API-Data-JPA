package com.intexsoft.devi.controller;

import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Locale;

/**
 * @author DEVIAPHAN
 * Processes the request and returns the response as JSON.
 */
@RestController
@RequestMapping("/teachers")
public class TeacherController {
    @Autowired
    TeacherService teacherService;

    @Autowired
    MessageSource messageSource;

    /**
     * @return getAll teacher entity in the database.
     */
    @GetMapping
    public List<Teacher> getAll(Locale locale) {
        return teacherService.getAll(locale);
    }


    /**
     * @param id of teacher
     * @return teacher entity by ID in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @GetMapping("/{id}")
    public Teacher getById(@PathVariable Long id, Locale locale) throws EntityNotFoundException {
        return teacherService.getById(id, locale);
    }

    /**
     * @param teacher entity
     * @return added teacher entity in the database.
     */
    @PostMapping
    public Teacher save(@RequestBody Teacher teacher, Locale locale) {
        return teacherService.save(teacher, locale);
    }

    /**
     * @param teacher entity
     * @param id      of teacher
     * @return updated teacher entity in the database.
     * @throws EntityNotFoundException if there is no value
     */
    @PutMapping("/{id}")
    public Teacher updateById(@RequestBody Teacher teacher, @PathVariable Long id, Locale locale) throws EntityNotFoundException {
        return teacherService.updateById(teacher, id, locale);
    }

    /**
     * @param id the teacher entity to be removed from the database
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id, Locale locale) {
        teacherService.deleteById(id, locale);
    }
}
