package com.example.taskmanagementapp.dto.notification;

import java.util.List;

public record TelegramUpdatesResponse(
        boolean ok,
        List<TelegramUpdateDto> result
) {
}