package com.example.taskmanagementapp.service;

import com.example.taskmanagementapp.model.Comment;
import com.example.taskmanagementapp.model.Task;

public interface NotificationService {
    void notifyTaskAssigned(Task task);

    void notifyNewComment(Comment comment);
}
