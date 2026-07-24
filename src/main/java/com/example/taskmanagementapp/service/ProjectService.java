package com.example.taskmanagementapp.service;

import com.example.taskmanagementapp.dto.project.CreateProjectRequestDto;
import com.example.taskmanagementapp.dto.project.ProjectResponseDto;
import com.example.taskmanagementapp.dto.project.UpdateProjectRequestDto;
import com.example.taskmanagementapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    ProjectResponseDto createProject(User user, CreateProjectRequestDto requestDto);

    Page<ProjectResponseDto> getAllProjects(User user, Pageable pageable);

    ProjectResponseDto getProjectById(Long id, User user);

    ProjectResponseDto updateProjectById(Long id, User user, UpdateProjectRequestDto requestDto);

    void deleteProjectById(Long id, User user);
}
