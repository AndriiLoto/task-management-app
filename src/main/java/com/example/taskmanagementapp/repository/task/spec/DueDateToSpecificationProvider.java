package com.example.taskmanagementapp.repository.task.spec;

import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.repository.SpecificationProvider;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class DueDateToSpecificationProvider implements SpecificationProvider<Task> {
    private static final String DUE_DATE_TO = "dueDateTo";
    private static final String DUE_DATE = "dueDate";

    @Override
    public String getKey() {
        return DUE_DATE_TO;
    }

    @Override
    public Specification<Task> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(
                        root.get(DUE_DATE),
                        LocalDate.parse(params[0]).plusDays(1).atStartOfDay()
                );
    }
}
