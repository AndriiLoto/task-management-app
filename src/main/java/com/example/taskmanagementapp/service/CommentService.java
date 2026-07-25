package com.example.taskmanagementapp.service;

import com.example.taskmanagementapp.dto.comment.CommentResponseDto;
import com.example.taskmanagementapp.dto.comment.CreateCommentRequestDto;
import com.example.taskmanagementapp.model.User;

public interface CommentService {

    CommentResponseDto createComment(User user, CreateCommentRequestDto requestDto);
}
