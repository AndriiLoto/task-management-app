package com.example.taskmanagementapp.repository;

import com.example.taskmanagementapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
