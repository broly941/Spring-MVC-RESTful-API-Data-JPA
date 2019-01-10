package com.intexsoft.devi.service;

import com.intexsoft.devi.beans.ValidationStatus;
import com.intexsoft.devi.entity.Teacher;
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
public interface TeacherService {
    List<Teacher> getAll(Locale locale);

    Teacher getById(Long id, Locale locale) throws EntityNotFoundException;

    List<Teacher> getTeachersOfGroupById(Long id, Locale locale) throws EntityNotFoundException;

    @Transactional
    Teacher save(Teacher teacher, Locale locale);

    @Transactional
    Teacher updateById(Teacher teacher, Long teacherId, Locale locale) throws EntityNotFoundException;

    void deleteById(Long id, Locale locale);

    Optional<Teacher> getTeacherByName(String firstName, String lastName);

    boolean fileValidation(Map<Integer, List<Object>> map, ValidationStatus validationStatus, Locale locale);

    void fileSave(Map<Integer, List<Object>> map, Locale locale);
}
