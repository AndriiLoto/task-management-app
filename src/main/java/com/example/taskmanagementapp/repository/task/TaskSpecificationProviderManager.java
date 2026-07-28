package com.example.taskmanagementapp.repository.task;

import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.repository.SpecificationProvider;
import com.example.taskmanagementapp.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskSpecificationProviderManager implements SpecificationProviderManager<Task> {
    private final List<SpecificationProvider<Task>> taskSpecificationProviders;

    @Override
    public SpecificationProvider<Task> getSpecificationProvider(String key) {
        return taskSpecificationProviders.stream()
                .filter(t -> t.getKey().equals(key))
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("Specification provider not found for key: "
                                + key)
                );
    }
}
