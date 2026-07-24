package com.example.taskmanagementapp.controller;

import com.example.taskmanagementapp.dto.project.CreateProjectRequestDto;
import com.example.taskmanagementapp.dto.project.ProjectResponseDto;
import com.example.taskmanagementapp.dto.project.UpdateProjectRequestDto;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Project", description = "Endpoints for project management")
public class ProjectController {
    private final ProjectService projectService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(summary = "Create new project",
            description = "Creates a new project for the authenticated user"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponseDto createProject(@AuthenticationPrincipal User user,
                                            @RequestBody @Valid CreateProjectRequestDto requestDto) {
        return projectService.createProject(user, requestDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all projects",
            description = "Get all projects for the authenticated user"
    )
    public Page<ProjectResponseDto> getAllProjects(@AuthenticationPrincipal User user, Pageable pageable) {
        return projectService.getAllProjects(user, pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get project by Id",
            description = "Get project by Id for current user"
    )
    public ProjectResponseDto getProjectById(@AuthenticationPrincipal User user,@PathVariable Long id) {
        return projectService.getProjectById(id,user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update project by id",
            description = "Update project by id for current user"
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ProjectResponseDto updateProjectById(@AuthenticationPrincipal User user, @PathVariable Long id,
                                                @RequestBody @Valid UpdateProjectRequestDto requestDto) {
        return projectService.updateProjectById(id, user, requestDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project by id",
            description = "Delete project by id for current user"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectById(@AuthenticationPrincipal User user, @PathVariable Long id) {
        projectService.deleteProjectById(id, user);
    }
}
