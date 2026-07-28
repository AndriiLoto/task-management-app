package com.example.taskmanagementapp.service.impl;

import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.service.NotificationService;
import com.example.taskmanagementapp.service.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final TelegramService telegramService;

    @Override
    public void notifyTaskAssigned(Task task) {
        User assignee = task.getAssignee();

        if (assignee == null || assignee.getTelegramChatId() == null) {
            return;
        }

        String message = """
                New task assigned!📋
                
                Task: %s
                Priority: %s
                Due Date: %s
                """.formatted(
                        task.getName(),
                task.getPriority().name(),
                task.getDueDate()
        );
        telegramService.sendMessage(assignee.getTelegramChatId(), message);
    }
}
