package com.example.taskmanagementapp.controller;

import static com.example.taskmanagementapp.util.TestDataFactory.NOW;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taskmanagementapp.dto.task.CreateTaskRequestDto;
import com.example.taskmanagementapp.dto.task.UpdateTaskRequestDto;
import com.example.taskmanagementapp.dto.task.UpdateTaskStatusDto;
import com.example.taskmanagementapp.model.TaskPriority;
import com.example.taskmanagementapp.model.TaskStatus;
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
                "classpath:database/add-tasks.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = {
                "classpath:database/remove-tasks.sql",
                "classpath:database/remove-projects.sql",
                "classpath:database/remove-users.sql"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
class TaskControllerTest {
    private static final long EXISTING_PROJECT_ID = 1L;
    private static final long EXISTING_TASK_ID = 1L;
    private static final long ASSIGNEE_ID = 2L;

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @MockitoBean
    private TelegramService telegramService;

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Create task should persist task and return created response")
    void createTask_WithValidRequest_ReturnsCreatedTask() throws Exception {
        CreateTaskRequestDto requestDto = new CreateTaskRequestDto();
        requestDto.setName("Integration Task");
        requestDto.setDescription("Created from integration test");
        requestDto.setPriority(TaskPriority.HIGH);
        requestDto.setDueDate(NOW.plusDays(4));
        requestDto.setProjectId(EXISTING_PROJECT_ID);
        requestDto.setAssigneeId(ASSIGNEE_ID);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Integration Task"))
                .andExpect(jsonPath("$.status").value("NOT_STARTED"))
                .andExpect(jsonPath("$.projectId").value(EXISTING_PROJECT_ID))
                .andExpect(jsonPath("$.assigneeId").value(ASSIGNEE_ID));
    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Get all tasks by project id should return seeded task page")
    void getAllTasks_WithProjectId_ReturnsTaskPage() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .param("projectId", String.valueOf(EXISTING_PROJECT_ID))
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(EXISTING_TASK_ID));
    }

    @Test
    @WithUserDetails(value = "assignee", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Get all tasks for current user should return assigned tasks")
    void getAllTasksForCurrentUser_ReturnsAssignedTaskPage() throws Exception {
        mockMvc.perform(get("/api/tasks/me")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].assigneeId").value(ASSIGNEE_ID));
    }

    @Test
    @WithUserDetails(value = "assignee", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Get task by id should return task when user has access")
    void getTaskById_WhenUserHasAccess_ReturnsTask() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", EXISTING_TASK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXISTING_TASK_ID))
                .andExpect(jsonPath("$.assigneeId").value(ASSIGNEE_ID));
    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Update task should persist changes and return accepted response")
    void updateTaskById_WithValidRequest_ReturnsAcceptedTask() throws Exception {
        UpdateTaskRequestDto requestDto = new UpdateTaskRequestDto();
        requestDto.setName("Updated Task");
        requestDto.setDescription("Updated task description");
        requestDto.setPriority(TaskPriority.LOW);

        mockMvc.perform(put("/api/tasks/{id}", EXISTING_TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(EXISTING_TASK_ID))
                .andExpect(jsonPath("$.name").value("Updated Task"))
                .andExpect(jsonPath("$.priority").value("LOW"));
    }

    @Test
    @WithUserDetails(value = "assignee", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Update task status should persist status and return accepted response")
    void updateTaskStatus_WithValidRequest_ReturnsAcceptedTask() throws Exception {
        UpdateTaskStatusDto requestDto = new UpdateTaskStatusDto();
        requestDto.setStatus(TaskStatus.COMPLETED);

        mockMvc.perform(put("/api/tasks/{id}/status", EXISTING_TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(EXISTING_TASK_ID))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Search tasks should return tasks matching query parameters")
    void search_WithQueryParameters_ReturnsTaskPage() throws Exception {
        mockMvc.perform(get("/api/tasks/search")
                        .param("status", "IN_PROGRESS")
                        .param("priority", "HIGH")
                        .param("name", "Implement"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(EXISTING_TASK_ID));
    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Delete task should return no content")
    void deleteTask_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/tasks/{taskId}", EXISTING_TASK_ID))
                .andExpect(status().isNoContent());
    }
}
