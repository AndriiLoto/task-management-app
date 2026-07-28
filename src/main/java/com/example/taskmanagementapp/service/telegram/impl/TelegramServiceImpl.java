package com.example.taskmanagementapp.service.telegram.impl;

import com.example.taskmanagementapp.dto.notification.TelegramMessageRequest;
import com.example.taskmanagementapp.dto.notification.TelegramUpdatesResponse;
import com.example.taskmanagementapp.service.telegram.TelegramService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TelegramServiceImpl implements TelegramService {

    private final RestClient restClient;

    public TelegramServiceImpl(@Value("${telegram.bot-token}") String botToken) {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.telegram.org/bot" + botToken)
                .build();
    }

    @Override
    public void sendMessage(Long chatId, String message) {
        TelegramMessageRequest request =
                new TelegramMessageRequest(chatId, message);

        restClient.post()
                .uri("/sendMessage")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public TelegramUpdatesResponse getUpdates(Long offset) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/getUpdates")
                        .queryParam("offset", offset)
                        .build())
                .retrieve()
                .body(TelegramUpdatesResponse.class);
    }
}
