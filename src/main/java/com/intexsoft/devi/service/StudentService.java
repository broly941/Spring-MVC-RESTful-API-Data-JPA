package com.intexsoft.devi.service;

import com.intexsoft.devi.controller.response.ValidationStatus;
import com.intexsoft.devi.entity.Student;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * @author DEVIAPHAN on 21.12.2018
 * @project university
 */
public interface StudentService {
    List<Student> getAll(Locale locale);

    Student getById(Long id, Locale locale);

    Optional<Student> getByName(String firstName, String lastName);

    @Transactional
    Student save(Student student, Long groupId, Locale locale);

    @Transactional
    Student save(Student student, Locale locale);

    @Transactional
    Student updateById(Student student, Long studentId, Long groupId, Locale locale);

    void deleteById(Long id, Locale locale);

    Optional<Student> getStudentByNameAndGroupName(String firstName, String lastName, String groupName);

    List<Student> getStudentsOfGroupById(Long id, Locale locale);

    ValidationStatus validate(Map<Integer, List<Object>> parsedEntities, Map<Integer, Object> validEntities, Locale locale);

    void save(Map<Integer, Object> validEntities, Locale locale);
}