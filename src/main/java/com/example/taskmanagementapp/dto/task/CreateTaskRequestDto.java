package com.example.taskmanagementapp.dto.task;

import com.example.taskmanagementapp.model.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateTaskRequestDto {
    @NotBlank(message = "Task name cannot be blank")
    private String name;
    @NotBlank(message = "Task description cannot be blank")
    private String description;
    @NotNull(message = "Task priority cannot be blank")
    private TaskPriority priority;
    @NotNull(message = "Task due date cannot be blank")
    private LocalDateTime dueDate;
    @NotNull(message = "Task project cannot be blank")
    private Long projectId;

    private Long assigneeId;
}
