package com.example.taskmanagementapp.controller;

import com.example.taskmanagementapp.dto.comment.CommentResponseDto;
import com.example.taskmanagementapp.dto.comment.CreateCommentRequestDto;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "Endpoints for comment management")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new comment",
            description = "Creates a new comment for a task"
    )
    public CommentResponseDto createComment(@AuthenticationPrincipal User user,
                                            @RequestBody @Valid CreateCommentRequestDto requestDto) {
        return commentService.createComment(user, requestDto);
    }

    @GetMapping
    @Operation(summary = "Get comments for task",
            description = "Get comments for task by id"
    )
    public Page<CommentResponseDto> getAllCommentsByTaskId(@AuthenticationPrincipal User user,
                                                           @RequestParam Long taskId,
                                                           Pageable pageable)
    {
        return commentService.getCommentsByTaskId(user, taskId, pageable);
    }
}
