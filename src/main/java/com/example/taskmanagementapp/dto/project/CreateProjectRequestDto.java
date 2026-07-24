package com.example.taskmanagementapp.dto.project;

import com.example.taskmanagementapp.validation.date.EndDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EndDate(startDateField = "startDate", endDateField = "endDate")
public class CreateProjectRequestDto {
    @NotBlank(message = "Project name cannot be blank")
    private String name;
    @NotBlank(message = "Project description cannot be blank")
    private String description;
    @NotNull(message = "Project start date cannot be blank")
    private LocalDateTime startDate;
    @NotNull(message = "Project end date cannot be blank")
    private LocalDateTime endDate;
}
