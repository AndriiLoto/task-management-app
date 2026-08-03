package com.example.taskmanagementapp.service;

import static com.example.taskmanagementapp.util.TestDataFactory.admin;
import static com.example.taskmanagementapp.util.TestDataFactory.comment;
import static com.example.taskmanagementapp.util.TestDataFactory.commentResponseDto;
import static com.example.taskmanagementapp.util.TestDataFactory.project;
import static com.example.taskmanagementapp.util.TestDataFactory.task;
import static com.example.taskmanagementapp.util.TestDataFactory.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.taskmanagementapp.dto.comment.CommentResponseDto;
import com.example.taskmanagementapp.dto.comment.CreateCommentRequestDto;
import com.example.taskmanagementapp.exception.CustomAccessException;
import com.example.taskmanagementapp.exception.EntityNotFoundException;
import com.example.taskmanagementapp.mapper.comment.CommentMapper;
import com.example.taskmanagementapp.model.Comment;
import com.example.taskmanagementapp.model.Project;
import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.repository.CommentRepository;
import com.example.taskmanagementapp.repository.task.TaskRepository;
import com.example.taskmanagementapp.service.impl.CommentServiceImpl;
import com.example.taskmanagementapp.service.impl.TaskAccessService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskAccessService taskAccessService;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("Create comment should check task access, fill relationships and notify")
    void createComment_WithAccessibleTask_SavesAndNotifies() {
        User user = user(1L);
        Project project = project(10L, user);
        Task task = task(20L, project, user);
        Comment mappedComment = new Comment();
        CreateCommentRequestDto requestDto = new CreateCommentRequestDto();
        requestDto.setTaskId(task.getId());
        requestDto.setText("Comment text");
        CommentResponseDto expected = commentResponseDto(1L);

        when(commentMapper.toComment(requestDto)).thenReturn(mappedComment);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(commentMapper.toCommentResponseDto(mappedComment)).thenReturn(expected);

        CommentResponseDto actual = commentService.createComment(user, requestDto);

        assertThat(actual).isEqualTo(expected);
        assertThat(mappedComment.getTask()).isEqualTo(task);
        assertThat(mappedComment.getUser()).isEqualTo(user);
        assertThat(mappedComment.getTimestamp()).isNotNull();
        verify(taskAccessService).checkAccess(user, task);
        verify(commentRepository).save(mappedComment);
        verify(notificationService).notifyNewComment(mappedComment);
    }

    @Test
    @DisplayName("Create comment should throw when target task is missing")
    void createComment_WhenTaskMissing_ThrowsEntityNotFoundException() {
        User user = user(1L);
        CreateCommentRequestDto requestDto = new CreateCommentRequestDto();
        requestDto.setTaskId(99L);
        requestDto.setText("Comment text");
        Comment mappedComment = new Comment();

        when(commentMapper.toComment(requestDto)).thenReturn(mappedComment);
        when(taskRepository.findById(requestDto.getTaskId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.createComment(user, requestDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Task not found with id 99");

        verify(commentRepository, never()).save(any(Comment.class));
        verify(notificationService, never()).notifyNewComment(any(Comment.class));
    }

    @Test
    @DisplayName("Delete comment should allow admins")
    void deleteCommentById_WhenUserIsAdmin_DeletesComment() {
        User author = user(1L);
        User admin = admin(2L);
        Comment comment = comment(30L, task(20L, project(10L, author), author), author);

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        commentService.deleteCommentById(comment.getId(), admin);

        verify(commentRepository).deleteById(comment.getId());
    }

    @Test
    @DisplayName("Delete comment should reject users who are neither admin nor author")
    void deleteCommentById_WhenUserIsNotAdminOrAuthor_ThrowsCustomAccessException() {
        User author = user(1L);
        User otherUser = user(2L);
        Comment comment = comment(30L, task(20L, project(10L, author), author), author);

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        assertThatThrownBy(() -> commentService.deleteCommentById(comment.getId(), otherUser))
                .isInstanceOf(CustomAccessException.class)
                .hasMessageContaining("permission");

        verify(commentRepository, never()).deleteById(comment.getId());
    }
}
