package com.intexsoft.devi.repository;

import com.intexsoft.devi.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author DEVIAPHAN
 * The class works with searching, retrieving and storing data from a database.
 */
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByFirstNameAndLastNameAndGroup_Number(String firstName, String lastName, String number);

    @Query(value = "SELECT * FROM teacher WHERE Exists (select 1 from groupteacher where groupteacher.GroupId = ?1 and teacher.TeacherId = groupteacher.TeacherId)", nativeQuery = true)
    List<Teacher> findAllTeachersOfGroupById(Long id);

    Optional<Teacher> findByFirstNameAndLastName(String firstName, String lastName);
}
