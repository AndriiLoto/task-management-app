package com.example.taskmanagementapp.service;

import com.example.taskmanagementapp.dto.attachment.AttachmentResponseDto;
import com.example.taskmanagementapp.model.User;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

    AttachmentResponseDto uploadAttachment(User user, Long taskId, MultipartFile file)
            throws FileUploadException;

    Page<AttachmentResponseDto> findAttachmentsByTaskId(User user, Long taskId, Pageable pageable);
}
