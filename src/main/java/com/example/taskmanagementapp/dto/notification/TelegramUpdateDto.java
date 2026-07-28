package com.example.taskmanagementapp.dto.notification;

public record TelegramUpdateDto(
        Long update_id,
        TelegramMessageDto message
) {
}