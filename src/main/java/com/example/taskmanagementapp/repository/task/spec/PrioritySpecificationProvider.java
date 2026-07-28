package com.example.taskmanagementapp.repository.task.spec;

import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PrioritySpecificationProvider implements SpecificationProvider<Task> {
    private static final String PRIORITY = "priority";

    @Override
    public String getKey() {
        return PRIORITY;
    }

    @Override
    public Specification<Task> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get(PRIORITY).in(Arrays.stream(params).toArray());
    }
}
