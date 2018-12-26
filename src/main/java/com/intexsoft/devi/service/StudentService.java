package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Student;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Locale;

/**
 * @author DEVIAPHAN on 21.12.2018
 * @project university
 */
public interface StudentService {
    List<Student> getAll(Locale locale);

    Student getById(Long id, Locale locale) throws EntityNotFoundException;

    @Transactional
    Student save(Student student, Long groupId, Locale locale);

    @Transactional
    Student updateById(Student student, Long studentId, Long groupId, Locale locale) throws EntityNotFoundException;

    void deleteById(Long id, Locale locale);
}
