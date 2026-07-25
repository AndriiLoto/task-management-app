package com.example.taskmanagementapp.service;

import com.dropbox.core.v2.files.FileMetadata;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface DropboxService {

    FileMetadata uploadFile(MultipartFile file) throws FileUploadException;
}
