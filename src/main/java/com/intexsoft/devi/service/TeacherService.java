package com.intexsoft.devi.service;

import com.intexsoft.devi.controller.response.ValidationStatus;
import com.intexsoft.devi.entity.Teacher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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

    ValidationStatus validate(ConcurrentHashMap<Integer, List<Object>> parsedEntities, ConcurrentHashMap<Integer, Object> validEntities, Locale locale);

    void save(Map<Integer, Object> validEntities, Locale locale);
}
