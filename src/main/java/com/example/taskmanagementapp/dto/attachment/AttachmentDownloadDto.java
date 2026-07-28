package com.example.taskmanagementapp.dto.attachment;

public record AttachmentDownloadDto(
        byte[] content,
        String fileName
) {
}
