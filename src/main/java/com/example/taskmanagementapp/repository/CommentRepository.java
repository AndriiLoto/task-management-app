package com.example.taskmanagementapp.repository;

import com.example.taskmanagementapp.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findCommentByTask_Id(Long taskId, Pageable pageable);
}
