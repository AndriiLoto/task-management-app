package com.example.taskmanagementapp.util;

import com.example.taskmanagementapp.dto.attachment.AttachmentResponseDto;
import com.example.taskmanagementapp.dto.comment.CommentResponseDto;
import com.example.taskmanagementapp.dto.label.LabelResponseDto;
import com.example.taskmanagementapp.dto.project.ProjectResponseDto;
import com.example.taskmanagementapp.dto.task.TaskResponseDto;
import com.example.taskmanagementapp.dto.user.UserResponseDto;
import com.example.taskmanagementapp.model.Attachment;
import com.example.taskmanagementapp.model.Comment;
import com.example.taskmanagementapp.model.Label;
import com.example.taskmanagementapp.model.Project;
import com.example.taskmanagementapp.model.ProjectStatus;
import com.example.taskmanagementapp.model.Role;
import com.example.taskmanagementapp.model.RoleName;
import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.model.TaskPriority;
import com.example.taskmanagementapp.model.TaskStatus;
import com.example.taskmanagementapp.model.User;
import java.time.LocalDateTime;
import java.util.Set;

public final class TestDataFactory {
    public static final LocalDateTime NOW = LocalDateTime.of(2026, 8, 3, 12, 0);

    private TestDataFactory() {
    }

    public static User user(Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("user" + id);
        user.setEmail("user" + id + "@example.com");
        user.setPassword("password");
        user.setFirstName("First");
        user.setLastName("Last");
        user.getRoles().add(role(RoleName.USER));
        return user;
    }

    public static User admin(Long id) {
        User user = user(id);
        user.getRoles().clear();
        user.getRoles().add(role(RoleName.ADMIN));
        return user;
    }

    public static Role role(RoleName roleName) {
        Role role = new Role();
        role.setName(roleName);
        return role;
    }

    public static Project project(Long id, User owner) {
        Project project = new Project();
        project.setId(id);
        project.setName("Project " + id);
        project.setDescription("Project description");
        project.setStartDate(NOW);
        project.setEndDate(NOW.plusDays(7));
        project.setStatus(ProjectStatus.INITIATED);
        project.setOwner(owner);
        return project;
    }

    public static Task task(Long id, Project project, User assignee) {
        Task task = new Task();
        task.setId(id);
        task.setName("Task " + id);
        task.setDescription("Task description");
        task.setPriority(TaskPriority.HIGH);
        task.setStatus(TaskStatus.NOT_STARTED);
        task.setDueDate(NOW.plusDays(2));
        task.setProject(project);
        task.setAssignee(assignee);
        return task;
    }

    public static Label label(Long id) {
        Label label = new Label();
        label.setId(id);
        label.setName("Label " + id);
        label.setColor("#123456");
        return label;
    }

    public static Comment comment(Long id, Task task, User user) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setTask(task);
        comment.setUser(user);
        comment.setText("Comment text");
        comment.setTimestamp(NOW);
        return comment;
    }

    public static Attachment attachment(Long id, Task task) {
        Attachment attachment = new Attachment();
        attachment.setId(id);
        attachment.setTask(task);
        attachment.setFileName("spec.pdf");
        attachment.setDropBoxFileId("dropbox-id");
        attachment.setUploadDate(NOW);
        return attachment;
    }

    public static ProjectResponseDto projectResponseDto(Long id) {
        ProjectResponseDto dto = new ProjectResponseDto();
        dto.setId(id);
        dto.setName("Project " + id);
        dto.setDescription("Project description");
        dto.setStartDate(NOW);
        dto.setEndDate(NOW.plusDays(7));
        dto.setStatus(ProjectStatus.INITIATED);
        dto.setOwnerId(1L);
        return dto;
    }

    public static TaskResponseDto taskResponseDto(Long id) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(id);
        dto.setName("Task " + id);
        dto.setDescription("Task description");
        dto.setPriority(TaskPriority.HIGH);
        dto.setStatus(TaskStatus.NOT_STARTED);
        dto.setDueDate(NOW.plusDays(2));
        dto.setProjectId(10L);
        dto.setAssigneeId(2L);
        dto.setLabels(Set.of());
        return dto;
    }

    public static LabelResponseDto labelResponseDto(Long id) {
        LabelResponseDto dto = new LabelResponseDto();
        dto.setId(id);
        dto.setName("Label " + id);
        dto.setColor("#123456");
        return dto;
    }

    public static CommentResponseDto commentResponseDto(Long id) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(id);
        dto.setTaskId(20L);
        dto.setUserId(1L);
        dto.setText("Comment text");
        dto.setTimestamp(NOW);
        return dto;
    }

    public static AttachmentResponseDto attachmentResponseDto(Long id) {
        AttachmentResponseDto dto = new AttachmentResponseDto();
        dto.setId(id);
        dto.setTaskId(20L);
        dto.setFileName("spec.pdf");
        dto.setUploadDate(NOW);
        return dto;
    }

    public static UserResponseDto userResponseDto(Long id) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(id);
        dto.setUsername("user" + id);
        dto.setEmail("user" + id + "@example.com");
        dto.setFirstName("First");
        dto.setLastName("Last");
        dto.setRoles(Set.of("USER"));
        return dto;
    }
}
