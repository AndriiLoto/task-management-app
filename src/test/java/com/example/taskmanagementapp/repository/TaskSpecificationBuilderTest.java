package com.example.taskmanagementapp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.taskmanagementapp.dto.task.TaskSearchParamDto;
import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.model.TaskPriority;
import com.example.taskmanagementapp.model.TaskStatus;
import com.example.taskmanagementapp.repository.task.TaskSpecificationBuilder;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class TaskSpecificationBuilderTest {
    @Mock
    private SpecificationProviderManager<Task> providerManager;
    @Mock
    private SpecificationProvider<Task> statusProvider;
    @Mock
    private SpecificationProvider<Task> priorityProvider;
    @Mock
    private SpecificationProvider<Task> assigneeProvider;
    @Mock
    private SpecificationProvider<Task> projectProvider;
    @Mock
    private SpecificationProvider<Task> dueDateFromProvider;
    @Mock
    private SpecificationProvider<Task> dueDateToProvider;
    @Mock
    private SpecificationProvider<Task> nameProvider;

    @Test
    @DisplayName("Build should request providers only for populated search fields")
    void build_WithAllSearchFields_RequestsExpectedProvidersAndValues() {
        Specification<Task> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();
        TaskSpecificationBuilder builder = new TaskSpecificationBuilder(providerManager);
        TaskSearchParamDto searchParams = new TaskSearchParamDto(
                TaskStatus.IN_PROGRESS,
                TaskPriority.HIGH,
                2L,
                10L,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 31),
                "Backend"
        );

        stubProvider("status", statusProvider, specification);
        stubProvider("priority", priorityProvider, specification);
        stubProvider("assigneeId", assigneeProvider, specification);
        stubProvider("projectId", projectProvider, specification);
        stubProvider("dueDateFrom", dueDateFromProvider, specification);
        stubProvider("dueDateTo", dueDateToProvider, specification);
        stubProvider("name", nameProvider, specification);

        Specification<Task> actual = builder.build(searchParams);

        assertThat(actual).isNotNull();
        verify(statusProvider).getSpecification(argThat(values -> values[0].equals("IN_PROGRESS")));
        verify(priorityProvider).getSpecification(argThat(values -> values[0].equals("HIGH")));
        verify(assigneeProvider).getSpecification(argThat(values -> values[0].equals("2")));
        verify(projectProvider).getSpecification(argThat(values -> values[0].equals("10")));
        verify(dueDateFromProvider).getSpecification(argThat(values -> values[0].equals("2026-08-01")));
        verify(dueDateToProvider).getSpecification(argThat(values -> values[0].equals("2026-08-31")));
        verify(nameProvider).getSpecification(argThat(values -> values[0].equals("Backend")));
    }

    @Test
    @DisplayName("Build should ignore blank task name")
    void build_WithBlankName_DoesNotRequestNameProvider() {
        TaskSpecificationBuilder builder = new TaskSpecificationBuilder(providerManager);
        TaskSearchParamDto searchParams = new TaskSearchParamDto(
                null,
                null,
                null,
                null,
                null,
                null,
                "   "
        );

        Specification<Task> actual = builder.build(searchParams);

        assertThat(actual).isNotNull();
        verify(providerManager, never()).getSpecificationProvider("name");
    }

    private void stubProvider(String key,
                              SpecificationProvider<Task> provider,
                              Specification<Task> specification) {
        when(providerManager.getSpecificationProvider(key)).thenReturn(provider);
        when(provider.getSpecification(org.mockito.ArgumentMatchers.any(String[].class)))
                .thenReturn(specification);
    }
}
