package com.example.taskmanagementapp.repository;

import com.example.taskmanagementapp.dto.task.TaskSearchParamDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {

    Specification<T> build(TaskSearchParamDto taskSearchParamDto);
}
