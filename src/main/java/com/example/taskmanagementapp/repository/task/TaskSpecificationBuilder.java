package com.example.taskmanagementapp.repository.task;

import com.example.taskmanagementapp.dto.task.TaskSearchParamDto;
import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.repository.SpecificationBuilder;
import com.example.taskmanagementapp.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskSpecificationBuilder implements SpecificationBuilder<Task> {
    private static final String STATUS = "status";
    private static final String PRIORITY = "priority";
    private static final String ASSIGNEE_ID = "assigneeId";
    private static final String PROJECT_ID = "projectId";
    private static final String DUE_DATE_FROM = "dueDateFrom";
    private static final String DUE_DATE_TO = "dueDateTo";
    private static final String NAME = "name";
    private final SpecificationProviderManager<Task> taskSpecificationProviderManager;

    @Override
    public Specification<Task> build(TaskSearchParamDto taskSearchParamDto) {
        Specification<Task> spec = ((root, query, criteriaBuilder)
                -> criteriaBuilder.conjunction());
        if (taskSearchParamDto.status() != null) {
            spec = spec.and(getSpecification(STATUS, taskSearchParamDto.status().name()));
        }

        if (taskSearchParamDto.priority() != null) {
            spec = spec.and(getSpecification(PRIORITY, taskSearchParamDto.priority().name()));
        }

        if (taskSearchParamDto.assigneeId() != null) {
            spec = spec.and(
                    getSpecification(ASSIGNEE_ID, taskSearchParamDto.assigneeId().toString())
            );
        }

        if (taskSearchParamDto.projectId() != null) {
            spec = spec.and(
                    getSpecification(PROJECT_ID, taskSearchParamDto.projectId().toString())
            );
        }

        if (taskSearchParamDto.dueDateFrom() != null) {
            spec = spec.and(
                    getSpecification(DUE_DATE_FROM, taskSearchParamDto.dueDateFrom().toString())
            );
        }

        if (taskSearchParamDto.dueDateTo() != null) {
            spec = spec.and(
                    getSpecification(DUE_DATE_TO, taskSearchParamDto.dueDateTo().toString())
            );
        }

        if (taskSearchParamDto.name() != null && !taskSearchParamDto.name().isBlank()) {
            spec = spec.and(getSpecification(NAME, taskSearchParamDto.name()));
        }
        return spec;
    }

    private Specification<Task> getSpecification(String key, String value) {
        return taskSpecificationProviderManager
                .getSpecificationProvider(key)
                .getSpecification(new String[]{value});
    }
}
