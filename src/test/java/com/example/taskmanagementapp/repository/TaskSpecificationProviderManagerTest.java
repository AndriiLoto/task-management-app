package com.example.taskmanagementapp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.repository.task.TaskSpecificationProviderManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

class TaskSpecificationProviderManagerTest {
    @Test
    @DisplayName("Get specification provider should return provider matching key")
    void getSpecificationProvider_WithExistingKey_ReturnsProvider() {
        SpecificationProvider<Task> nameProvider = provider("name");
        SpecificationProvider<Task> statusProvider = provider("status");
        TaskSpecificationProviderManager manager = new TaskSpecificationProviderManager(
                List.of(nameProvider, statusProvider)
        );

        SpecificationProvider<Task> actual = manager.getSpecificationProvider("status");

        assertThat(actual).isSameAs(statusProvider);
    }

    @Test
    @DisplayName("Get specification provider should throw when key is unsupported")
    void getSpecificationProvider_WithMissingKey_ThrowsRuntimeException() {
        TaskSpecificationProviderManager manager = new TaskSpecificationProviderManager(
                List.of(provider("name"))
        );

        assertThatThrownBy(() -> manager.getSpecificationProvider("unknown"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Specification provider not found for key: unknown");
    }

    private SpecificationProvider<Task> provider(String key) {
        SpecificationProvider<Task> provider = org.mockito.Mockito.mock(SpecificationProvider.class);
        when(provider.getKey()).thenReturn(key);
        when(provider.getSpecification(org.mockito.ArgumentMatchers.any(String[].class)))
                .thenReturn((Specification<Task>) (root, query, criteriaBuilder) ->
                        criteriaBuilder.conjunction());
        return provider;
    }
}
