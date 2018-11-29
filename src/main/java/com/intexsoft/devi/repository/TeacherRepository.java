package com.intexsoft.devi.repository;

import com.intexsoft.devi.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author DEVIAPHAN
 * The class works with searching, retrieving and storing data from a database.
 */
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

}
