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
        Project project = projectRepository.findByIdAndOwnerId(requestDto.getProjectId(),
                user.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Project not found with id "
                        + requestDto.getProjectId())
        );
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
}
