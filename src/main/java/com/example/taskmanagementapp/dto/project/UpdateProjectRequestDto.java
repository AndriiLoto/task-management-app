package com.example.taskmanagementapp.dto.project;

import com.example.taskmanagementapp.model.ProjectStatus;
import com.example.taskmanagementapp.validation.date.EndDate;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EndDate(startDateField = "startDate", endDateField = "endDate")
public class UpdateProjectRequestDto {

    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private ProjectStatus status;
}
