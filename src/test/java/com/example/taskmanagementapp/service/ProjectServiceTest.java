package com.example.taskmanagementapp.service;

import static com.example.taskmanagementapp.util.TestDataFactory.project;
import static com.example.taskmanagementapp.util.TestDataFactory.projectResponseDto;
import static com.example.taskmanagementapp.util.TestDataFactory.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.taskmanagementapp.dto.project.CreateProjectRequestDto;
import com.example.taskmanagementapp.dto.project.ProjectResponseDto;
import com.example.taskmanagementapp.dto.project.UpdateProjectRequestDto;
import com.example.taskmanagementapp.exception.EntityNotFoundException;
import com.example.taskmanagementapp.mapper.project.ProjectMapper;
import com.example.taskmanagementapp.model.Project;
import com.example.taskmanagementapp.model.ProjectStatus;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.repository.ProjectRepository;
import com.example.taskmanagementapp.service.impl.ProjectServiceImpl;
import com.example.taskmanagementapp.util.TestDataFactory;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectMapper projectMapper;
    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    @DisplayName("Create project should set owner and INITIATED status before saving")
    void createProject_WithValidRequest_SetsOwnerAndStatus() {
        User owner = user(1L);
        CreateProjectRequestDto requestDto = new CreateProjectRequestDto();
        Project project = new Project();
        ProjectResponseDto expected = projectResponseDto(10L);

        when(projectMapper.toProject(requestDto)).thenReturn(project);
        when(projectMapper.toProjectResponseDto(project)).thenReturn(expected);

        ProjectResponseDto actual = projectService.createProject(owner, requestDto);

        assertThat(actual).isEqualTo(expected);
        assertThat(project.getOwner()).isEqualTo(owner);
        assertThat(project.getStatus()).isEqualTo(ProjectStatus.INITIATED);
        verify(projectRepository).save(project);
    }

    @Test
    @DisplayName("Get project by id should use owner-scoped lookup")
    void getProjectById_WhenProjectExists_ReturnsMappedDto() {
        User owner = user(1L);
        Project project = project(10L, owner);
        ProjectResponseDto expected = projectResponseDto(10L);

        when(projectRepository.findByIdAndOwnerId(project.getId(), owner.getId()))
                .thenReturn(Optional.of(project));
        when(projectMapper.toProjectResponseDto(project)).thenReturn(expected);

        ProjectResponseDto actual = projectService.getProjectById(project.getId(), owner);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Get project by id should throw when owner-scoped project is missing")
    void getProjectById_WhenProjectMissing_ThrowsEntityNotFoundException() {
        User owner = user(1L);
        when(projectRepository.findByIdAndOwnerId(99L, owner.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.getProjectById(99L, owner))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Project not found with id 99");
    }

    @Test
    @DisplayName("Update project should map changes, save entity and return mapped dto")
    void updateProjectById_WhenProjectExists_UpdatesAndSaves() {
        User owner = user(1L);
        Project project = project(10L, owner);
        UpdateProjectRequestDto requestDto = new UpdateProjectRequestDto();
        ProjectResponseDto expected = projectResponseDto(10L);

        when(projectRepository.findByIdAndOwnerId(project.getId(), owner.getId()))
                .thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toProjectResponseDto(project)).thenReturn(expected);

        ProjectResponseDto actual = projectService.updateProjectById(
                project.getId(),
                owner,
                requestDto
        );

        assertThat(actual).isEqualTo(expected);
        verify(projectMapper).updateProjectFromDto(requestDto, project);
        verify(projectRepository).save(project);
    }
}
