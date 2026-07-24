package com.example.taskmanagementapp.service.impl;

import com.example.taskmanagementapp.dto.project.CreateProjectRequestDto;
import com.example.taskmanagementapp.dto.project.ProjectResponseDto;
import com.example.taskmanagementapp.dto.project.UpdateProjectRequestDto;
import com.example.taskmanagementapp.exception.EntityNotFoundException;
import com.example.taskmanagementapp.mapper.project.ProjectMapper;
import com.example.taskmanagementapp.model.Project;
import com.example.taskmanagementapp.model.ProjectStatus;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.repository.ProjectRepository;
import com.example.taskmanagementapp.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectResponseDto createProject(User user, CreateProjectRequestDto requestDto) {
        Project project = projectMapper.toProject(requestDto);
        project.setOwner(user);
        project.setStatus(ProjectStatus.INITIATED);
        projectRepository.save(project);
        return projectMapper.toProjectResponseDto(project);
    }

    @Override
    public Page<ProjectResponseDto> getAllProjects(User user, Pageable pageable) {
        return projectRepository.getAllByOwnerId(user.getId(), pageable)
                .map(projectMapper::toProjectResponseDto);
    }

    @Override
    public ProjectResponseDto getProjectById(Long id,User user) {
        Project project = getProjectByIdAndUserId(id, user);
        return projectMapper.toProjectResponseDto(project);
    }

    @Override
    public ProjectResponseDto updateProjectById(Long id, User user,
                                                UpdateProjectRequestDto requestDto) {
        Project project = getProjectByIdAndUserId(id, user);
        projectMapper.updateProjectFromDto(requestDto, project);
        Project updatedProject = projectRepository.save(project);
        return projectMapper.toProjectResponseDto(updatedProject);
    }

    @Override
    public void deleteProjectById(Long id, User user) {
        Project project = getProjectByIdAndUserId(id, user);
        projectRepository.delete(project);
    }

    private Project getProjectByIdAndUserId(Long id, User user) {
        return projectRepository.findByIdAndOwnerId(id, user.getId()).orElseThrow(
                () -> new EntityNotFoundException("Project not found with id " + id)
        );
    }
}
