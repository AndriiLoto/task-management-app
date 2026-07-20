package com.example.taskmanagementapp.repository;

import com.example.taskmanagementapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsernameOrEmail(String userName, String email);
}
