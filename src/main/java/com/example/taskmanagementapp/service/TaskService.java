package com.example.taskmanagementapp.service;

import com.example.taskmanagementapp.dto.task.CreateTaskRequestDto;
import com.example.taskmanagementapp.dto.task.TaskResponseDto;
import com.example.taskmanagementapp.model.User;

public interface TaskService {
    TaskResponseDto createTask(User user, CreateTaskRequestDto requestDto);
}
