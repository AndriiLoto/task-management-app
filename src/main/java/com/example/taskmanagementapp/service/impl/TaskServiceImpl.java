package com.example.taskmanagementapp.service.impl;

import com.example.taskmanagementapp.dto.task.CreateTaskRequestDto;
import com.example.taskmanagementapp.dto.task.TaskResponseDto;
import com.example.taskmanagementapp.exception.EntityNotFoundException;
import com.example.taskmanagementapp.mapper.task.TaskMapper;
import com.example.taskmanagementapp.model.Project;
import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.model.TaskStatus;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.repository.ProjectRepository;
import com.example.taskmanagementapp.repository.TaskRepository;
import com.example.taskmanagementapp.repository.UserRepository;
import com.example.taskmanagementapp.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskResponseDto createTask(User user, CreateTaskRequestDto requestDto) {
        Project project = getProject(user, requestDto.getProjectId());
        Task task = taskMapper.toTask(requestDto);
        task.setProject(project);
        if (requestDto.getAssigneeId() != null) {
            User assignee = userRepository.findById(requestDto.getAssigneeId()).orElseThrow(
                    () -> new EntityNotFoundException("User not found with id "
                            + requestDto.getAssigneeId())
            );
            task.setAssignee(assignee);
        }
        task.setStatus(TaskStatus.NOT_STARTED);
        taskRepository.save(task);
        return taskMapper.toTaskResponseDto(task);
    }

    @Override
    public Page<TaskResponseDto> getAllTasksByProjectId(User user, Long projectId, Pageable pageable) {
        Project project = getProject(user, projectId);
        return taskRepository.getTasksByProjectId(project.getId(), pageable)
                .map(taskMapper::toTaskResponseDto);
    }

    @Override
    public Page<TaskResponseDto> getAllTasksForCurrentUser(User user, Pageable pageable) {
        return taskRepository.getTasksByAssigneeId(user.getId(), pageable)
                .map(taskMapper::toTaskResponseDto);
    }

    @Override
    public TaskResponseDto getTaskById(Long id, User user) {
        return taskRepository.findById(id)
                .map(taskMapper::toTaskResponseDto)
                .orElseThrow(
                        () -> new EntityNotFoundException("Task not found with id " + id)
                );
    }

    private Project getProject(User user, Long projectId) {
        return projectRepository.findByIdAndOwnerId(projectId,
                user.getId()).orElseThrow(
                () -> new EntityNotFoundException("Project not found with id "
                        + projectId)
        );
    }
}
