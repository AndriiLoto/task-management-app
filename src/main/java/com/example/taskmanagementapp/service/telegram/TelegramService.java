package com.example.taskmanagementapp.service.telegram;

import com.example.taskmanagementapp.dto.notification.TelegramUpdatesResponse;

public interface TelegramService {
    void sendMessage(Long chatId, String message);

    TelegramUpdatesResponse getUpdates(Long offset);
}
