package com.example.taskmanagementapp.controller;

import com.example.taskmanagementapp.dto.task.CreateTaskRequestDto;
import com.example.taskmanagementapp.dto.task.TaskResponseDto;
import com.example.taskmanagementapp.dto.task.TaskSearchParamDto;
import com.example.taskmanagementapp.dto.task.UpdateTaskRequestDto;
import com.example.taskmanagementapp.dto.task.UpdateTaskStatusDto;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto createTask(@AuthenticationPrincipal User user,
                                      @RequestBody @Valid CreateTaskRequestDto requestDto
    ) {
        return taskService.createTask(user, requestDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all tasks by project id",
            description = "Get all tasks by project id for authenticated user"
    )
    public Page<TaskResponseDto> getAllTasks(@AuthenticationPrincipal User user,
                                             @RequestParam Long projectId, Pageable pageable) {
        return taskService.getAllTasksByProjectId(user, projectId, pageable);
    }

    @GetMapping("/me")
    @Operation(summary = "Get all tasks by assignee id",
            description = "Get all tasks by project id for authenticated user"
    )
    public Page<TaskResponseDto> getAllTasksForCurrentUser(@AuthenticationPrincipal User user,
                                                           Pageable pageable) {
        return taskService.getAllTasksForCurrentUser(user, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by id",
            description = "Get task by id for authenticated user"
    )
    public TaskResponseDto getTaskById(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return taskService.getTaskById(id, user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update task info", description = "Update task info by id")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TaskResponseDto updateTaskByID(@PathVariable Long id, @AuthenticationPrincipal User user,
                                          @RequestBody @Valid UpdateTaskRequestDto requestDto) {
        return taskService.updateTaskById(id, user, requestDto);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update task status", description = "Update task status by id")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TaskResponseDto updateTaskStatus(@PathVariable Long id,
                                            @AuthenticationPrincipal User user,
                                          @RequestBody @Valid UpdateTaskStatusDto requestDto) {
        return taskService.updateTaskStatus(id, user, requestDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long taskId,
                       @AuthenticationPrincipal User user) {
        taskService.deleteTaskById(taskId, user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{taskId}/labels/{labelId}")
    @Operation(summary = "Add label to task", description = "Add label to task by id")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TaskResponseDto addLabelToTask(@AuthenticationPrincipal User user,
                                          @PathVariable Long taskId,
                                          @PathVariable Long labelId
    ) {
        return taskService.addLabelToTask(taskId, user, labelId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{taskId}/labels/{labelId}")
    @Operation(summary = "Delete label from task", description = "Delete label from task by id")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TaskResponseDto deleteLabelFromTask(@AuthenticationPrincipal User user,
                                          @PathVariable Long taskId,
                                          @PathVariable Long labelId) {
        return taskService.removeLabelFromTask(taskId, user, labelId);
    }

    @GetMapping("/search")
    @Operation(summary = "Search tasks", description = "Search tasks using specified parameters")
    public Page<TaskResponseDto> search(TaskSearchParamDto searchParamDto, Pageable pageable) {
       return taskService.search(searchParamDto, pageable);
    }
}
