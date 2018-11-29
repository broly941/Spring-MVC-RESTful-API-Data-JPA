package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Teacher;
import com.intexsoft.devi.exception.EntityNotFoundException;
import com.intexsoft.devi.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author DEVIAPHAN
 * Business Logic Service Class
 */
@Service
public class TeacherService {
    @Autowired
    TeacherRepository teacherRepository;

    /**
     * @return all teacher entity in the database.
     */
    public List<Teacher> all() {
        return teacherRepository.findAll();
    }

    /**
     * @param id
     * @return teacher entity by ID in the database.
     * @throws Exception if there is no value
     */
    public Teacher get(Long id) throws Exception {
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        if (teacherOptional.isPresent()) {
            return teacherOptional.get();
        }
        throw new EntityNotFoundException(Teacher.class, "teacherId", id.toString());
    }

    /**
     * @param teacher
     * @return added teacher entity in the database.
     */
    @Transactional
    public Teacher add(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    /**
     * @param teacher
     * @param teacherId
     * @return updated teacher entity in the database.
     * @throws Exception if there is no value
     */
    @Transactional
    public Teacher update(Teacher teacher, Long teacherId) throws Exception {
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);
        if (!teacherOptional.isPresent()) {
            throw new EntityNotFoundException(Teacher.class, "teacherId", teacherId.toString());
        }

        Teacher currentTeacher = teacherOptional.get();
        currentTeacher.setFirstName(teacher.getFirstName());
        currentTeacher.setLastName(teacher.getLastName());
        return teacherRepository.save(currentTeacher);
    }

    /**
     * @param id the teacher entity to be removed from the database
     */
    public void delete(Long id) {
        teacherRepository.deleteById(id);
    }
}
