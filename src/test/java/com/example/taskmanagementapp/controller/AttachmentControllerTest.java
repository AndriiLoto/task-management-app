package com.example.taskmanagementapp.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dropbox.core.v2.files.FileMetadata;
import com.example.taskmanagementapp.service.DropboxService;
import com.example.taskmanagementapp.service.telegram.TelegramService;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(
        scripts = {
                "classpath:database/add-users.sql",
                "classpath:database/add-projects.sql",
                "classpath:database/add-tasks.sql",
                "classpath:database/add-attachments.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = {
                "classpath:database/remove-attachments.sql",
                "classpath:database/remove-tasks.sql",
                "classpath:database/remove-projects.sql",
                "classpath:database/remove-users.sql"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
@WithUserDetails(value = "assignee", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class AttachmentControllerTest {
    private static final long EXISTING_TASK_ID = 1L;
    private static final long EXISTING_ATTACHMENT_ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private DropboxService dropboxService;
    @MockitoBean
    private TelegramService telegramService;

    @Test
    @DisplayName("Upload attachment should persist metadata and return created response")
    void uploadAttachment_WithMultipartRequest_ReturnsCreatedAttachment() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "upload.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "content".getBytes()
        );
        when(dropboxService.uploadFile(any())).thenReturn(new FileMetadata(
                "upload.pdf",
                "dropbox-new-id",
                new Date(),
                new Date(),
                "abcdef123",
                file.getSize()
        ));

        mockMvc.perform(multipart("/api/attachments")
                        .file(file)
                        .param("taskId", String.valueOf(EXISTING_TASK_ID)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.taskId").value(EXISTING_TASK_ID))
                .andExpect(jsonPath("$.fileName").value("upload.pdf"))
                .andExpect(jsonPath("$.uploadDate").exists());
    }

    @Test
    @DisplayName("Get attachments by task id should return seeded attachment page")
    void getAllAttachmentsByTaskId_ReturnsAttachmentPage() throws Exception {
        mockMvc.perform(get("/api/attachments")
                        .param("taskId", String.valueOf(EXISTING_TASK_ID))
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(EXISTING_ATTACHMENT_ID))
                .andExpect(jsonPath("$.content[0].fileName").value("requirements.pdf"));
    }

    @Test
    @DisplayName("Download attachment should return file bytes and download headers")
    void downloadAttachment_ReturnsBytesAndDownloadHeaders() throws Exception {
        when(dropboxService.downloadFile("dropbox-file-id")).thenReturn("content".getBytes());

        mockMvc.perform(get("/api/attachments/{id}/download", EXISTING_ATTACHMENT_ID))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"requirements.pdf\""
                ))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes("content".getBytes()));
    }

    @Test
    @DisplayName("Delete attachment should remove metadata and return no content")
    void deleteAttachmentById_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/attachments/{id}", EXISTING_ATTACHMENT_ID))
                .andExpect(status().isNoContent());
    }
}
