package com.example.taskmanagementapp.service.impl;

import com.example.taskmanagementapp.dto.comment.CommentResponseDto;
import com.example.taskmanagementapp.dto.comment.CreateCommentRequestDto;
import com.example.taskmanagementapp.exception.CustomAccessException;
import com.example.taskmanagementapp.exception.EntityNotFoundException;
import com.example.taskmanagementapp.mapper.comment.CommentMapper;
import com.example.taskmanagementapp.model.Comment;
import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.repository.CommentRepository;
import com.example.taskmanagementapp.repository.TaskRepository;
import com.example.taskmanagementapp.service.CommentService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TaskRepository taskRepository;
    private final TaskAccessService taskAccessService;

    @Override
    public CommentResponseDto createComment(User user, CreateCommentRequestDto requestDto) {
        Comment comment = commentMapper.toComment(requestDto);
        Task task = taskRepository.findById(requestDto.getTaskId()).orElseThrow(
                () -> new EntityNotFoundException("Task not found with id "
                        + requestDto.getTaskId())
        );
        taskAccessService.checkAccess(user, task);
        comment.setTask(task);
        comment.setUser(user);
        comment.setTimestamp(LocalDateTime.now());
        commentRepository.save(comment);
        return commentMapper.toCommentResponseDto(comment);
    }

    @Override
    public Page<CommentResponseDto> getCommentsByTaskId(User user, Long taskId, Pageable pageable) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Task not found with id " + taskId)
        );
        taskAccessService.checkAccess(user, task);
        return commentRepository.findCommentByTask_Id(taskId, pageable)
                .map(commentMapper::toCommentResponseDto);
    }

    @Override
    public void deleteCommentById(Long id, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment not found with id " + id)
        );

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));

        boolean isAssignee = comment.getUser() != null
                && comment.getUser().getId().equals(user.getId());

        if (!isAdmin && !isAssignee) {
            throw new CustomAccessException("User don't have permission to delete this comment");
        }
        commentRepository.deleteById(id);
    }

}
