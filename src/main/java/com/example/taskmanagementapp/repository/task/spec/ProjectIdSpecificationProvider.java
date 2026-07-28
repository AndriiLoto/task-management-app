package com.example.taskmanagementapp.repository.task.spec;

import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ProjectIdSpecificationProvider implements SpecificationProvider<Task> {
    private static final String PROJECT_ID = "projectId";
    private static final String PROJECT = "project";
    private static final String ID = "id";

    @Override
    public String getKey() {
        return PROJECT_ID;
    }

    @Override
    public Specification<Task> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get(PROJECT).get(ID).in(Arrays.stream(params).toArray());
    }
}
