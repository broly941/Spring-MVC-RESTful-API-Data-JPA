package com.intexsoft.devi.service;

import com.intexsoft.devi.controller.response.ValidationStatus;
import com.intexsoft.devi.entity.Teacher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * @author DEVIAPHAN on 21.12.2018
 * @project university
 */
public interface TeacherService {
    List<Teacher> getAll(Locale locale);

    Teacher getById(Long id, Locale locale);

    List<Teacher> getTeachersOfGroupById(Long id, Locale locale);

    @Transactional
    Teacher save(Teacher teacher, Locale locale);

    @Transactional
    Teacher updateById(Teacher teacher, Long teacherId, Locale locale);

    void deleteById(Long id, Locale locale);

    Optional<Teacher> getTeacherByName(String firstName, String lastName);

    ValidationStatus validate(Map<Integer, List<Object>> parsedEntities, Map<Integer, Object> validEntities, Locale locale);

    void save(Map<Integer, Object> validEntities, Locale locale);
}
