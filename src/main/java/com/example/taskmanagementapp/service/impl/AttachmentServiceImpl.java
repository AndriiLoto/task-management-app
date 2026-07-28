package com.example.taskmanagementapp.service.impl;

import com.dropbox.core.v2.files.FileMetadata;
import com.example.taskmanagementapp.dto.attachment.AttachmentResponseDto;
import com.example.taskmanagementapp.exception.EntityNotFoundException;
import com.example.taskmanagementapp.mapper.attachment.AttachmentMapper;
import com.example.taskmanagementapp.model.Attachment;
import com.example.taskmanagementapp.model.Task;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.repository.AttachmentRepository;
import com.example.taskmanagementapp.repository.TaskRepository;
import com.example.taskmanagementapp.service.AttachmentService;
import com.example.taskmanagementapp.service.DropboxService;
import java.time.LocalDateTime;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final DropboxService dropboxService;
    private final AttachmentRepository attachmentRepository;
    private final TaskAccessService taskAccessService;
    private final TaskRepository taskRepository;
    private final AttachmentMapper attachmentMapper;

    @Override
    @Transactional
    public AttachmentResponseDto uploadAttachment(User user, Long taskId, MultipartFile file)
            throws FileUploadException {
        Task task = getTaskById(taskId);
        taskAccessService.checkAccess(user,task);
        FileMetadata fileMetadata = dropboxService.uploadFile(file);
        Attachment attachment = new Attachment();
        attachment.setDropBoxFileId(fileMetadata.getId());
        attachment.setFileName(file.getOriginalFilename());
        attachment.setUploadDate(LocalDateTime.now());
        attachment.setTask(task);
        attachmentRepository.save(attachment);
        return attachmentMapper.toAttachmentResponseDto(attachment);
    }

    @Override
    public Page<AttachmentResponseDto> findAttachmentsByTaskId(User user, Long taskId, Pageable pageable) {
        Task task = getTaskById(taskId);
        taskAccessService.checkAccess(user, task);
        return attachmentRepository.findAllByTaskId(taskId, pageable)
                .map(attachmentMapper::toAttachmentResponseDto);
    }

    private Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Task not found with id " + taskId)
        );
    }
}
