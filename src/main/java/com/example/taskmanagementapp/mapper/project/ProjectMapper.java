package com.example.taskmanagementapp.mapper.project;

import com.example.taskmanagementapp.config.MapperConfig;
import com.example.taskmanagementapp.dto.project.CreateProjectRequestDto;
import com.example.taskmanagementapp.dto.project.ProjectResponseDto;
import com.example.taskmanagementapp.dto.project.UpdateProjectRequestDto;
import com.example.taskmanagementapp.model.Project;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class,
        nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public interface ProjectMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    ProjectResponseDto toProjectResponseDto(Project project);

    Project toProject(CreateProjectRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProjectFromDto(UpdateProjectRequestDto requestDto, @MappingTarget Project project);
}
