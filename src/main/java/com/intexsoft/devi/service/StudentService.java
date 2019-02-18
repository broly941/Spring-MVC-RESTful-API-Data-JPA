package com.intexsoft.devi.service;

import com.intexsoft.devi.controller.request.RequestParameters;
import com.intexsoft.devi.controller.response.ValidationStatus;
import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author DEVIAPHAN on 21.12.2018
 * @project university
 */
public interface StudentService {
    List<Student> getAll(Locale locale);

    Student getById(Long id, Locale locale);

    Optional<Student> getByName(String firstName, String lastName);

    Student save(Student student, Long groupId, Locale locale);

    Student save(Student student, Locale locale);

    Student updateById(Student student, Long studentId, Long groupId, Locale locale);

    void deleteById(Long id, Locale locale);

    Optional<Student> getStudentByNameAndGroupName(String firstName, String lastName, String groupName);

    List<Student> getStudentsOfGroupById(Long id, Locale locale);

    ValidationStatus validate(ConcurrentHashMap<Integer, List<Object>> parsedEntities, ConcurrentHashMap<Integer, Object> validEntities, Locale locale);

    void save(Map<Integer, Object> validEntities, Locale locale);

    List<Student> getSortedStudents();

    List<Student> getSortedRevertStudents();

    Page<Student> getByFilter(RequestParameters parameters, Locale locale);
}