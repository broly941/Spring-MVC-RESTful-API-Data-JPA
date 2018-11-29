package com.intexsoft.devi.controller;

import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;

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

    private static final Logger logger =
            LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    MessageSource messageSource;

    /**
     * @return all teacher entity in the database.
     */
    @GetMapping("")
    public List<Teacher> listAllTeachers() {
        logger.info(
                messageSource.getMessage("messages.all", new Object[] {"Teachers"}, Locale.ENGLISH)
        );
        return teacherService.all();
    }

    /**
     * @param id
     * @return teacher entity by ID in the database.
     * @throws Exception if there is no value
     */
    @GetMapping("/{id}")
    public Teacher getTeacher(@PathVariable Long id) throws Exception {
        logger.info("get teacher by id: " + id);
        return teacherService.get(id);
    }

    /**
     * @param teacher
     * @return added teacher entity in the database.
     */
    @PostMapping("")
    public Teacher addTeacher(@RequestBody Teacher teacher) {
        logger.info("add teacher " + teacher.getFirstName() + " " + teacher.getLastName());
        return teacherService.add(teacher);
    }

    /**
     * @param teacher
     * @param id
     * @return updated teacher entity in the database.
     * @throws Exception if there is no value
     */
    @PutMapping("/{id}")
    public Teacher updateTeacher(@RequestBody Teacher teacher, @PathVariable Long id) throws Exception {
        logger.info("update teacher " + teacher.getFirstName() + " " + teacher.getLastName());
        return teacherService.update(teacher, id);
    }

    /**
     * @param id the teacher entity to be removed from the database
     */
    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable Long id) {
        logger.info("delete teacher by id: " + id);
        teacherService.delete(id);
    }
}
