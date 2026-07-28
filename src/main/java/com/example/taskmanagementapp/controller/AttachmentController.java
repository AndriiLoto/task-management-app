package com.example.taskmanagementapp.controller;

import com.example.taskmanagementapp.dto.attachment.AttachmentDownloadDto;
import com.example.taskmanagementapp.dto.attachment.AttachmentResponseDto;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
@Tag(name = "Attachment", description = "Endpoints for attachment management")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload attachment", description = "Uploads an attachment to a task")
    public AttachmentResponseDto uploadAttachment(@AuthenticationPrincipal User user,
                                                  @RequestParam Long taskId,
                                                  MultipartFile file) throws FileUploadException {
        return attachmentService.uploadAttachment(user, taskId, file);
    }

    @GetMapping()
    @Operation(summary = "Get all attachments by task id",
            description = "Get all attachments by task id")
    public Page<AttachmentResponseDto> getAllAttachmentsByTaskId(@AuthenticationPrincipal User user,
                                                                 @RequestParam Long taskId,
                                                                 Pageable pageable) {
        return attachmentService.findAttachmentsByTaskId(user, taskId, pageable);
    }

    @GetMapping("{id}/download")
    @Operation(summary = "Download attachment", description = "Downloads an attachment by id")
    public ResponseEntity<byte[]> downloadAttachment(@AuthenticationPrincipal User user,
                                                     @PathVariable Long id) {
        AttachmentDownloadDto attachmentDownloadDto =
                attachmentService.downloadAttachment(user, id);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachmentDownloadDto.fileName() + "\""
                )
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(attachmentDownloadDto.content());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete attachment", description = "Deletes an attachment by id")
    public void deleteAttachmentById(@AuthenticationPrincipal User user,
                                                   @PathVariable Long id) {
        attachmentService.deleteAttachment(id, user);
    }
}
