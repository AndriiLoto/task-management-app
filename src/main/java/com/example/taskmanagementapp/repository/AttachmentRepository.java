package com.example.taskmanagementapp.repository;

import com.example.taskmanagementapp.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
