package com.example.taskmanagementapp.mapper.attachment;

import com.example.taskmanagementapp.config.MapperConfig;
import com.example.taskmanagementapp.dto.attachment.AttachmentResponseDto;
import com.example.taskmanagementapp.model.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(config = MapperConfig.class,
        nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public interface AttachmentMapper {

    @Mapping(source = "task.id", target = "taskId")
    AttachmentResponseDto toAttachmentResponseDto(Attachment attachment);
}
