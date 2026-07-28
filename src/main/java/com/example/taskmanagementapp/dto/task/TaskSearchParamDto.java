package com.example.taskmanagementapp.dto.task;

import com.example.taskmanagementapp.model.TaskPriority;
import com.example.taskmanagementapp.model.TaskStatus;
import java.time.LocalDate;

public record TaskSearchParamDto(
        TaskStatus status,
        TaskPriority priority,
        Long assigneeId,
        Long projectId,
        LocalDate dueDateFrom,
        LocalDate dueDateTo,
        String name
) {
}
