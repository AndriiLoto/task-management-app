package com.example.taskmanagementapp.service.impl;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.example.taskmanagementapp.exception.FileStorageException;
import com.example.taskmanagementapp.service.DropboxService;
import java.io.ByteArrayOutputStream;
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

    @Override
    public byte[] downloadFile(String dropboxFileId) {
        try (DbxDownloader<FileMetadata> downloader =
                     dbxClient.files()
                             .downloadBuilder(dropboxFileId)
                             .start();
                    InputStream inputStream = downloader.getInputStream();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            inputStream.transferTo(outputStream);
            return outputStream.toByteArray();

        } catch (IOException | DbxException e) {
            throw new FileStorageException("Failed to download file from Dropbox", e);
        }
    }

    @Override
    public void deleteFile(String dropboxFileId) {
        try {
            dbxClient.files().deleteV2(dropboxFileId);
        } catch (DbxException e) {
            throw new FileStorageException("Failed to delete file from Dropbox", e);
        }
    }
}
