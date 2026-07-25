package com.example.taskmanagementapp.service;

import com.example.taskmanagementapp.dto.comment.CommentResponseDto;
import com.example.taskmanagementapp.dto.comment.CreateCommentRequestDto;
import com.example.taskmanagementapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    CommentResponseDto createComment(User user, CreateCommentRequestDto requestDto);

    Page<CommentResponseDto> getCommentsByTaskId(User user, Long taskId, Pageable pageable);

    void deleteCommentById(Long id, User user);
}
