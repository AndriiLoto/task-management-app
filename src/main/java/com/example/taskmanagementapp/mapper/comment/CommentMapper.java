package com.example.taskmanagementapp.mapper.comment;

import com.example.taskmanagementapp.config.MapperConfig;
import com.example.taskmanagementapp.dto.comment.CommentResponseDto;
import com.example.taskmanagementapp.dto.comment.CreateCommentRequestDto;
import com.example.taskmanagementapp.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(config = MapperConfig.class,
        nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public interface CommentMapper {
    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "user.id", target = "userId")
    CommentResponseDto toCommentResponseDto(Comment comment);

    @Mapping(target = "task", ignore = true)
    @Mapping(target = "user", ignore = true)
    Comment toComment(CreateCommentRequestDto requestDto);
}
