package com.example.taskmanagementapp.dto.notification;

public record TelegramMessageRequest(
        Long chat_id,
        String text
) {
}
