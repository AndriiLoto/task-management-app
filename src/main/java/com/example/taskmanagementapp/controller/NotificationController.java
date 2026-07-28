package com.example.taskmanagementapp.controller;

import com.example.taskmanagementapp.service.telegram.TelegramService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "Endpoints for notification management")
public class NotificationController {
    private final TelegramService telegramService;

    @PostMapping("/telegram")
    public void post() {
        telegramService.sendMessage(309757771L, "Task Management Bot is working!");
    }
}
