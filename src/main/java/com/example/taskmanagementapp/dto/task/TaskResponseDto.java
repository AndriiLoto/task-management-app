package com.example.taskmanagementapp.dto.task;

import com.example.taskmanagementapp.dto.label.LabelResponseDto;
import com.example.taskmanagementapp.model.TaskPriority;
import com.example.taskmanagementapp.model.TaskStatus;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskResponseDto {
    private Long id;
    private String name;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime dueDate;
    private Long projectId;
    private Long assigneeId;
    private Set<LabelResponseDto> labels;
}
