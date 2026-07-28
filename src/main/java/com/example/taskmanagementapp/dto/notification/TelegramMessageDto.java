package com.example.taskmanagementapp.dto.notification;

public record TelegramMessageDto(
        TelegramChatDto chat,
        String text
) {
}
