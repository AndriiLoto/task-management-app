package com.example.taskmanagementapp.service;

import com.example.taskmanagementapp.dto.task.CreateTaskRequestDto;
import com.example.taskmanagementapp.dto.task.TaskResponseDto;
import com.example.taskmanagementapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskResponseDto createTask(User user, CreateTaskRequestDto requestDto);

    Page<TaskResponseDto> getAllTasksByProjectId(User user, Long projectId, Pageable pageable);

    Page<TaskResponseDto> getAllTasksForCurrentUser(User user, Pageable pageable);

    TaskResponseDto getTaskById(Long id, User user);
}
