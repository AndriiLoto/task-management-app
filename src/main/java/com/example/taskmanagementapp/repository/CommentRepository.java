package com.example.taskmanagementapp.repository;

import com.example.taskmanagementapp.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
