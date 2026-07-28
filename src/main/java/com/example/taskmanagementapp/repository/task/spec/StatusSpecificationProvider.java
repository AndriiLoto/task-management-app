package com.example.taskmanagementapp.repository.task.spec;

import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class StatusSpecificationProvider implements SpecificationProvider<Task> {
    private static final String STATUS = "status";

    @Override
    public String getKey() {
        return STATUS;
    }

    @Override
    public Specification<Task> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get(STATUS).in(Arrays.stream(params).toArray());
    }
}
