package com.intexsoft.devi.repository;

import com.intexsoft.devi.entity.Role;
import com.intexsoft.devi.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    Optional<Role> findByName(RoleName role);
}
