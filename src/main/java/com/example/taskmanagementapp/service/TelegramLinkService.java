package com.example.taskmanagementapp.service;

import com.example.taskmanagementapp.model.User;

public interface TelegramLinkService {
    String generateLinkToken(User user);

    void linkTelegram(String token, Long chatId);
}
