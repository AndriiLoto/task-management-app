package com.example.taskmanagementapp.repository;

import com.example.taskmanagementapp.model.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Page<Attachment> findAllByTaskId(Long taskId, Pageable pageable);
}
