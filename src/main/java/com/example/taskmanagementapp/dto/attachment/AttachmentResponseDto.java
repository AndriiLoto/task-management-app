package com.example.taskmanagementapp.dto.attachment;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AttachmentResponseDto {
    private Long id;
    private Long taskId;
    private String fileName;
    private LocalDateTime uploadDate;
}
