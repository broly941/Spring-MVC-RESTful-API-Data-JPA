package com.intexsoft.devi.controller;

import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/university/teachers")
public class TeacherController {
    @Autowired
    TeacherService teacherService;

    @Autowired
    MessageSource messageSource;

    private static final Logger LOGGER =
            LoggerFactory.getLogger(TeacherService.class);

    /**
     * @return getAll teacher entity in the database.
     */
    @GetMapping("")
    public List<Teacher> getAll(@RequestHeader("Accept-Language") Locale locale) {
        return teacherService.getAll(locale);
    }


    /**
     * @param id
     * @return teacher entity by ID in the database.
     * @throws Exception if there is no value
     */
    @GetMapping("/{id}")
    public Teacher getById(@PathVariable Long id, @RequestHeader("Accept-Language") Locale locale) throws EntityNotFoundException {
        return teacherService.getById(id, locale);
    }

    /**
     * @param teacher
     * @return added teacher entity in the database.
     */
    @PostMapping("")
    public Teacher save(@RequestBody Teacher teacher, @RequestHeader("Accept-Language") Locale locale) {
        return teacherService.save(teacher, locale);
    }

    /**
     * @param teacher
     * @param id
     * @return updated teacher entity in the database.
     * @throws Exception if there is no value
     */
    @PutMapping("/{id}")
    public Teacher updateById(@RequestBody Teacher teacher, @PathVariable Long id, @RequestHeader("Accept-Language") Locale locale) throws EntityNotFoundException {
        return teacherService.updateById(teacher, id, locale);
    }

    /**
     * @param id the teacher entity to be removed from the database
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id, @RequestHeader("Accept-Language") Locale locale) {
        teacherService.deleteById(id, locale);
    }
}
