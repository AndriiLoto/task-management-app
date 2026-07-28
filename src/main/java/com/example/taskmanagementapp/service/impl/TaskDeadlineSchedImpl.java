package com.example.taskmanagementapp.service.impl;

import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.model.TaskStatus;
import com.example.taskmanagementapp.repository.task.TaskRepository;
import com.example.taskmanagementapp.service.NotificationService;
import com.example.taskmanagementapp.service.TaskDeadlineSched;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskDeadlineSchedImpl implements TaskDeadlineSched {
    private final TaskRepository taskRepository;
    private final NotificationService notificationService;

    @Override
    @Scheduled(cron = "0 30 9 * * *")
    public void checkDeadlines() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

        List<Task> tasks = taskRepository
                .findAllByDueDateAndStatusNot(tomorrow, TaskStatus.COMPLETED);

        for (Task task : tasks) {
            notificationService.notifyTaskDeadline(task);
        }
    }
}
