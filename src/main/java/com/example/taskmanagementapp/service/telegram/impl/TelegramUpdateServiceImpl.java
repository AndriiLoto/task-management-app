package com.example.taskmanagementapp.service.telegram.impl;

import com.example.taskmanagementapp.dto.notification.TelegramUpdateDto;
import com.example.taskmanagementapp.dto.notification.TelegramUpdatesResponse;
import com.example.taskmanagementapp.service.telegram.TelegramLinkService;
import com.example.taskmanagementapp.service.telegram.TelegramService;
import com.example.taskmanagementapp.service.telegram.TelegramUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramUpdateServiceImpl implements TelegramUpdateService {
    private final TelegramService telegramService;
    private final TelegramLinkService telegramLinkService;

    private long offset = 0;
    @Override
    @Scheduled(fixedDelay = 3000)
    public void getChatId() {
        TelegramUpdatesResponse response = telegramService.getUpdates(offset);

        if (response == null || response.result().isEmpty()) {
            return;
        }

        for (TelegramUpdateDto update : response.result()) {
            offset = update.update_id() + 1;
            if (update.message() == null || update.message().text() == null) {
                continue;
            }
            String text = update.message().text();
            if (!text.startsWith("/start ")) {
                continue;
            }
            String token = text.substring("/start".length()).trim();
            Long chatId = update.message().chat().id();
            telegramLinkService.linkTelegram(token, chatId);
            telegramService.sendMessage(chatId, "You have successfully linked your Telegram account! ✅");
        }

    }
}
