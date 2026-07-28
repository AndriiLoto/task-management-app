package com.example.taskmanagementapp.exception;

public class TelegramLinkTokenExpiredException extends RuntimeException {
    public TelegramLinkTokenExpiredException(String message) {
        super(message);
    }
}
