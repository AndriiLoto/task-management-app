package com.example.taskmanagementapp.dto.project;

import com.example.taskmanagementapp.model.ProjectStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProjectResponseDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ProjectStatus status;
    private Long ownerId;
}
