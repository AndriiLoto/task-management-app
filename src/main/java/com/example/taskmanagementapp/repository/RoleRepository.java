package com.example.taskmanagementapp.repository;

import com.example.taskmanagementapp.model.Role;
import com.example.taskmanagementapp.model.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
