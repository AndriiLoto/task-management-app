package com.example.taskmanagementapp.repository.task.spec;

import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AssigneeIdSpecificationProvider implements SpecificationProvider<Task> {
    private static final String ASSIGNEE_ID = "assigneeId";
    private static final String ASSIGNEE = "assignee";
    private static final String ID = "id";

    @Override
    public String getKey() {
        return ASSIGNEE_ID;
    }

    @Override
    public Specification<Task> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get(ASSIGNEE).get(ID).in(Arrays.stream(params).toArray());
    }
}
