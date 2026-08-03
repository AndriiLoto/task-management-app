package com.example.taskmanagementapp.service;

import static com.example.taskmanagementapp.util.TestDataFactory.label;
import static com.example.taskmanagementapp.util.TestDataFactory.project;
import static com.example.taskmanagementapp.util.TestDataFactory.task;
import static com.example.taskmanagementapp.util.TestDataFactory.taskResponseDto;
import static com.example.taskmanagementapp.util.TestDataFactory.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.taskmanagementapp.dto.task.CreateTaskRequestDto;
import com.example.taskmanagementapp.dto.task.TaskResponseDto;
import com.example.taskmanagementapp.dto.task.UpdateTaskStatusDto;
import com.example.taskmanagementapp.exception.CustomAccessException;
import com.example.taskmanagementapp.exception.EntityNotFoundException;
import com.example.taskmanagementapp.exception.LabelNotAssignedException;
import com.example.taskmanagementapp.mapper.task.TaskMapper;
import com.example.taskmanagementapp.model.Label;
import com.example.taskmanagementapp.model.Project;
import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.model.TaskPriority;
import com.example.taskmanagementapp.model.TaskStatus;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.repository.LabelRepository;
import com.example.taskmanagementapp.repository.ProjectRepository;
import com.example.taskmanagementapp.repository.UserRepository;
import com.example.taskmanagementapp.repository.task.TaskRepository;
import com.example.taskmanagementapp.repository.task.TaskSpecificationBuilder;
import com.example.taskmanagementapp.service.impl.TaskAccessService;
import com.example.taskmanagementapp.service.impl.TaskServiceImpl;
import com.example.taskmanagementapp.util.TestDataFactory;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private TaskAccessService taskAccessService;
    @Mock
    private LabelRepository labelRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private TaskSpecificationBuilder taskSpecificationBuilder;
    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    @DisplayName("Create task should attach project, assignee, default status and send assignment notification")
    void createTask_WithAssignee_CreatesTaskAndNotifies() {
        User owner = user(1L);
        User assignee = user(2L);
        Project project = project(10L, owner);
        Task mappedTask = new Task();
        CreateTaskRequestDto requestDto = createTaskRequest(project.getId(), assignee.getId());
        TaskResponseDto expected = taskResponseDto(20L);

        when(projectRepository.findByIdAndOwnerId(project.getId(), owner.getId()))
                .thenReturn(Optional.of(project));
        when(taskMapper.toTask(requestDto)).thenReturn(mappedTask);
        when(userRepository.findById(assignee.getId())).thenReturn(Optional.of(assignee));
        when(taskMapper.toTaskResponseDto(mappedTask)).thenReturn(expected);

        TaskResponseDto actual = taskService.createTask(owner, requestDto);

        assertThat(actual).isEqualTo(expected);
        assertThat(mappedTask.getProject()).isEqualTo(project);
        assertThat(mappedTask.getAssignee()).isEqualTo(assignee);
        assertThat(mappedTask.getStatus()).isEqualTo(TaskStatus.NOT_STARTED);
        verify(taskRepository).save(mappedTask);
        verify(notificationService).notifyTaskAssigned(mappedTask);
    }

    @Test
    @DisplayName("Create task should fail when owner-scoped project is missing")
    void createTask_WhenProjectMissing_ThrowsEntityNotFoundException() {
        User owner = user(1L);
        CreateTaskRequestDto requestDto = createTaskRequest(99L, null);
        when(projectRepository.findByIdAndOwnerId(requestDto.getProjectId(), owner.getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.createTask(owner, requestDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Project not found with id 99");

        verify(taskRepository, never()).save(any(Task.class));
        verify(notificationService, never()).notifyTaskAssigned(any(Task.class));
    }

    @Test
    @DisplayName("Update task status should save and notify when current user is assignee")
    void updateTaskStatus_WhenAssigneeChangesStatus_SavesAndNotifies() {
        User owner = user(1L);
        User assignee = user(2L);
        Task task = task(20L, project(10L, owner), assignee);
        TaskResponseDto expected = taskResponseDto(20L);
        UpdateTaskStatusDto requestDto = new UpdateTaskStatusDto();
        requestDto.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toTaskResponseDto(task)).thenReturn(expected);

        TaskResponseDto actual = taskService.updateTaskStatus(task.getId(), assignee, requestDto);

        assertThat(actual).isEqualTo(expected);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        verify(taskRepository).save(task);
        verify(notificationService).notifyTaskStatusUpdate(task, TaskStatus.NOT_STARTED);
    }

    @Test
    @DisplayName("Update task status should reject users who are not the assignee")
    void updateTaskStatus_WhenUserIsNotAssignee_ThrowsCustomAccessException() {
        User owner = user(1L);
        User assignee = user(2L);
        User stranger = user(3L);
        Task task = task(20L, project(10L, owner), assignee);
        UpdateTaskStatusDto requestDto = new UpdateTaskStatusDto();
        requestDto.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.updateTaskStatus(task.getId(), stranger, requestDto))
                .isInstanceOf(CustomAccessException.class)
                .hasMessageContaining("User is not assignee");

        verify(taskRepository, never()).save(any(Task.class));
        verify(notificationService, never()).notifyTaskStatusUpdate(any(Task.class), any());
    }

    @Test
    @DisplayName("Remove label should throw when label is not assigned to task")
    void removeLabelFromTask_WhenLabelNotAssigned_ThrowsLabelNotAssignedException() {
        User owner = user(1L);
        Task task = task(20L, project(10L, owner), user(2L));
        Label label = label(5L);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(labelRepository.findById(label.getId())).thenReturn(Optional.of(label));

        assertThatThrownBy(() -> taskService.removeLabelFromTask(task.getId(), owner, label.getId()))
                .isInstanceOf(LabelNotAssignedException.class)
                .hasMessageContaining("is not assigned to task");

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Get task by id should check access before returning mapped dto")
    void getTaskById_WhenTaskExists_ChecksAccessAndReturnsDto() {
        User owner = user(1L);
        Task task = task(20L, project(10L, owner), user(2L));
        TaskResponseDto expected = taskResponseDto(20L);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskMapper.toTaskResponseDto(task)).thenReturn(expected);

        TaskResponseDto actual = taskService.getTaskById(task.getId(), owner);

        assertThat(actual).isEqualTo(expected);
        verify(taskAccessService).checkAccess(owner, task);
    }

    private CreateTaskRequestDto createTaskRequest(Long projectId, Long assigneeId) {
        CreateTaskRequestDto requestDto = new CreateTaskRequestDto();
        requestDto.setName("Task");
        requestDto.setDescription("Description");
        requestDto.setPriority(TaskPriority.HIGH);
        requestDto.setDueDate(TestDataFactory.NOW.plusDays(2));
        requestDto.setProjectId(projectId);
        requestDto.setAssigneeId(assigneeId);
        return requestDto;
    }
}
