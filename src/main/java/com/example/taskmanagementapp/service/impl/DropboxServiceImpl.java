package com.example.taskmanagementapp.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.example.taskmanagementapp.service.DropboxService;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DropboxServiceImpl implements DropboxService {
    private static final String UPLOAD_PATH = "/attachments/";

    private final DbxClientV2 dbxClient;

    @Override
    public FileMetadata uploadFile(MultipartFile file) throws FileUploadException {
        String path = UPLOAD_PATH
                + UUID.randomUUID()
                + "-"
                + file.getOriginalFilename();
        try (InputStream inputStream = file.getInputStream()) {
            return dbxClient.files()
                    .uploadBuilder(path)
                    .uploadAndFinish(inputStream);
        } catch (DbxException | IOException e) {
            throw new FileUploadException("Failed to upload file to Dropbox", e);
        }
    }
}
