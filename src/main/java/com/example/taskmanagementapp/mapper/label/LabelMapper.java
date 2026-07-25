package com.example.taskmanagementapp.mapper.label;

import com.example.taskmanagementapp.config.MapperConfig;
import com.example.taskmanagementapp.dto.label.CreateLabelRequestDto;
import com.example.taskmanagementapp.dto.label.LabelResponseDto;
import com.example.taskmanagementapp.dto.label.UpdateLabelRequestDto;
import com.example.taskmanagementapp.model.Label;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class,
        nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public interface LabelMapper {

    LabelResponseDto toLabelResponseDto(Label label);

    Label toLabel(CreateLabelRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateLabelFromDto(UpdateLabelRequestDto requestDto, @MappingTarget Label label);
}
