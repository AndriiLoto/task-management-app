package com.example.taskmanagementapp.dto.task;

import com.example.taskmanagementapp.model.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateTaskStatusDto {
    @NotNull(message = "Task status cannot be blank")
    private TaskStatus status;
}
