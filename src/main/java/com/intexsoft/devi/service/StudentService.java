package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Student;
import com.intexsoft.devi.exception.EntityNotFoundException;
import com.intexsoft.devi.repository.StudentsRepository;
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
public class StudentService {
    @Autowired
    StudentsRepository studentsRepository;

    @Autowired
    GroupService groupService;

    /**
     * @return all student entities in the database.
     */
    public List<Student> all() {
        return studentsRepository.findAll();
    }

    /**
     * @param id
     * @return student entity by ID in the database.
     */
    public Student get(Long id) throws Exception {
        Optional<Student> studentsOptional = studentsRepository.findById(id);
        if (studentsOptional.isPresent()) {
            return studentsOptional.get();
        }
        throw new EntityNotFoundException(Student.class, "studentId", id.toString());
    }

    /**
     *
     * @param student
     * @param groupId
     * @return added student entity in the database.
     * @throws Exception if there is no value
     */
    @Transactional
    public Student add(Student student, Long groupId) throws Exception {
        student.setGroup(groupService.get(groupId));
        return studentsRepository.save(student);
    }

    /**
     *
     * @param student
     * @param studentId
     * @param groupId
     * @return updated student entity in the database.
     * @throws Exception if there is no value
     */
    @Transactional
    public Student update(Student student, Long studentId, Long groupId) throws Exception {
        Optional<Student> studentsOptional = studentsRepository.findById(studentId);
        if (!studentsOptional.isPresent()){
            throw new EntityNotFoundException(Student.class, "studentId", student.toString());
        }

        Student currentStudent = studentsOptional.get();
        currentStudent.setFirstName(student.getFirstName());
        currentStudent.setLastName(student.getLastName());
        currentStudent.setGroup(groupService.get(groupId));
        return studentsRepository.save(currentStudent);
    }

    /**
     *
     * @param id the student entity to be removed from the database
     */
    public void delete(Long id) {
        studentsRepository.deleteById(id);
    }
}
