package com.example.taskmanagementapp.controller;

import com.example.taskmanagementapp.dto.task.CreateTaskRequestDto;
import com.example.taskmanagementapp.dto.task.TaskResponseDto;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
@Tag(name = "Task", description = "Endpoints for task management")
public class TaskController {
    private final TaskService taskService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(summary = "Create Task",
            description = "Create a new task by the authenticated user"
    )
    public TaskResponseDto createTask(@AuthenticationPrincipal User user,
                                      @RequestBody CreateTaskRequestDto requestDto
    ) {
        return taskService.createTask(user, requestDto);
    }
}
