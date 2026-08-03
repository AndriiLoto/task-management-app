package com.example.taskmanagementapp.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taskmanagementapp.dto.comment.CreateCommentRequestDto;
import com.example.taskmanagementapp.service.telegram.TelegramService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
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
                "classpath:database/add-comments.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = {
                "classpath:database/remove-comments.sql",
                "classpath:database/remove-tasks.sql",
                "classpath:database/remove-projects.sql",
                "classpath:database/remove-users.sql"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
class CommentControllerTest {
    private static final long EXISTING_TASK_ID = 1L;
    private static final long EXISTING_COMMENT_ID = 1L;
    private static final long ASSIGNEE_ID = 2L;

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @MockitoBean
    private TelegramService telegramService;

    @Test
    @WithUserDetails(
            value = "assignee",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    @DisplayName("Create comment should persist comment and return created response")
    void createComment_WithValidRequest_ReturnsCreatedComment() throws Exception {
        CreateCommentRequestDto requestDto = new CreateCommentRequestDto();
        requestDto.setTaskId(EXISTING_TASK_ID);
        requestDto.setText("Integration comment");

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.taskId").value(EXISTING_TASK_ID))
                .andExpect(jsonPath("$.userId").value(ASSIGNEE_ID))
                .andExpect(jsonPath("$.text").value("Integration comment"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @WithUserDetails(
            value = "assignee",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    @DisplayName("Get comments by task id should return seeded comments page")
    void getAllCommentsByTaskId_ReturnsCommentPage() throws Exception {
        mockMvc.perform(get("/api/comments")
                        .param("taskId", String.valueOf(EXISTING_TASK_ID))
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(EXISTING_COMMENT_ID))
                .andExpect(jsonPath("$.content[0].taskId").value(EXISTING_TASK_ID))
                .andExpect(jsonPath("$.content[0].userId").value(ASSIGNEE_ID))
                .andExpect(jsonPath("$.content[0].text").value("Initial dummy comment"));
    }

    @Test
    @WithUserDetails(
            value = "assignee",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    @DisplayName("Delete own comment should return no content")
    void deleteCommentById_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/comments/{id}", EXISTING_COMMENT_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(
            value = "assignee",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    @DisplayName("Create comment with blank text should return bad request")
    void createComment_WithBlankText_ReturnsBadRequest() throws Exception {
        CreateCommentRequestDto requestDto = new CreateCommentRequestDto();
        requestDto.setTaskId(EXISTING_TASK_ID);
        requestDto.setText(" ");

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").exists());
    }

    @Test
    @WithUserDetails(
            value = "assignee",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    @DisplayName("Get comments for missing task should return not found")
    void getAllCommentsByTaskId_WithMissingTask_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/comments")
                        .param("taskId", "999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found with id 999"));
    }
}
