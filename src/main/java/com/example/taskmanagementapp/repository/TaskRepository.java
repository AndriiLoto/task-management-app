package com.example.taskmanagementapp.repository;

import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> getTasksByProjectId(Long projectId, Pageable pageable);

    Page<Task> getTasksByAssigneeId(Long assigneeId, Pageable pageable);

    List<Task> findAllByDueDateAndStatusNot(LocalDateTime dueDate, TaskStatus status);
}
