package com.example.taskmanagementapp.service;

import com.example.taskmanagementapp.model.Comment;
import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.model.TaskStatus;

public interface NotificationService {
    void notifyTaskAssigned(Task task);

    void notifyNewComment(Comment comment);

    void notifyTaskReassigned(Task task);

    void notifyTaskStatusUpdate(Task task, TaskStatus oldStatus);

    void notifyTaskDeadline(Task task);
}
