package com.example.taskmanagementapp.repository;

import com.example.taskmanagementapp.model.Project;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> getAllByOwnerId(Long ownerId, Pageable pageable);

    Optional<Project> findByIdAndOwnerId(Long id, Long ownerId);
}
