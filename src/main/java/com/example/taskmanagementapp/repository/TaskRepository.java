package com.example.taskmanagementapp.repository;

import com.example.taskmanagementapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
