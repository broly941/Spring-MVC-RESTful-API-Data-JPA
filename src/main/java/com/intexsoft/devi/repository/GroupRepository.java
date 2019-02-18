package com.intexsoft.devi.repository;

import com.intexsoft.devi.entity.Group;
import com.intexsoft.devi.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author DEVIAPHAN
 * The class works with searching, retrieving and storing data from a database.
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByNumber(String number);

    @Query(value = "SELECT * FROM groupofuniversity WHERE Exists (select * from groupteacher where groupteacher.TeacherId = ?1 and groupofuniversity.GroupId = groupteacher.GroupId);", nativeQuery = true)
    List<Group> findAllGroupsOfTeacherById(Long id);

    Page<Group> findAll(Pageable pageable);

    Page<Group> findAllByNumber (String groupNumber, Pageable pageable);
}