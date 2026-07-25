package com.example.taskmanagementapp.service.impl;

import com.example.taskmanagementapp.exception.CustomAccessException;
import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class TaskAccessService {

    public void checkAccess(User user, Task task) {
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));

        boolean isAssignee = task.getAssignee() != null
                && task.getAssignee().getId().equals(user.getId());

        if (!isAdmin && !isAssignee) {
            throw new CustomAccessException("User don't have permission to access this task");
        }
    }
}
