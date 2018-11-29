package com.intexsoft.devi.repository;

import com.intexsoft.devi.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author DEVIAPHAN
 * The class works with searching, retrieving and storing data from a database.
 */
public interface StudentsRepository extends JpaRepository<Student, Long> {

}
