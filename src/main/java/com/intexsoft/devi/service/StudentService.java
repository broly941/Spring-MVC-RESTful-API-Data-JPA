package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Student;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

    Student getById(Long id, Locale locale) throws EntityNotFoundException;

    Optional<Student> getByName(String firstName, String lastName);

    @Transactional
    Student save(Student student, Long groupId, Locale locale);

    @Transactional
    Student save(Student student, Locale locale);

    @Transactional
    Student updateById(Student student, Long studentId, Long groupId, Locale locale) throws EntityNotFoundException;

    void deleteById(Long id, Locale locale);

    boolean isStudentGroupExist(String firstName, String lastName, String groupName);

    List<Student> getStudentsOfGroupById(Long id, Locale locale);

    boolean fileValidation(Map<Integer, List<Object>> map, StringBuilder validationStatus);

    void fileSave(Map<Integer, List<Object>> map, Locale locale);
}