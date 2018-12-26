package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Teacher;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Locale;

/**
 * @author DEVIAPHAN on 21.12.2018
 * @project university
 */
public interface TeacherService {
    List<Teacher> getAll(Locale locale);

    Teacher getById(Long id, Locale locale) throws EntityNotFoundException;

    @Transactional
    Teacher save(Teacher teacher, Locale locale);

    @Transactional
    Teacher updateById(Teacher teacher, Long teacherId, Locale locale) throws EntityNotFoundException;

    void deleteById(Long id, Locale locale);
}
