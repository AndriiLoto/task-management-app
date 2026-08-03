package com.example.taskmanagementapp.controller;

import static com.example.taskmanagementapp.util.TestDataFactory.NOW;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taskmanagementapp.dto.project.CreateProjectRequestDto;
import com.example.taskmanagementapp.dto.project.UpdateProjectRequestDto;
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
                "classpath:database/add-projects.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = {
                "classpath:database/remove-projects.sql",
                "classpath:database/remove-users.sql"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
@WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class ProjectControllerTest {
    private static final long EXISTING_PROJECT_ID = 1L;
    private static final long ADMIN_ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @MockitoBean
    private TelegramService telegramService;

    @Test
    @DisplayName("Create project should persist project and return created response")
    void createProject_WithValidRequest_ReturnsCreatedProject() throws Exception {
        CreateProjectRequestDto requestDto = new CreateProjectRequestDto();
        requestDto.setName("Integration Project");
        requestDto.setDescription("Created from integration test");
        requestDto.setStartDate(NOW.plusDays(1));
        requestDto.setEndDate(NOW.plusDays(3));

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Integration Project"))
                .andExpect(jsonPath("$.description").value("Created from integration test"))
                .andExpect(jsonPath("$.status").value("INITIATED"))
                .andExpect(jsonPath("$.ownerId").value(ADMIN_ID));
    }

    @Test
    @DisplayName("Get all projects should return seeded project page")
    void getAllProjects_ReturnsProjectPage() throws Exception {
        mockMvc.perform(get("/api/projects")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(EXISTING_PROJECT_ID))
                .andExpect(jsonPath("$.content[0].name").value("Seed Project"));
    }

    @Test
    @DisplayName("Get project by id should return seeded project")
    void getProjectById_WhenProjectExists_ReturnsProject() throws Exception {
        mockMvc.perform(get("/api/projects/{id}", EXISTING_PROJECT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXISTING_PROJECT_ID))
                .andExpect(jsonPath("$.ownerId").value(ADMIN_ID));
    }

    @Test
    @DisplayName("Update project should persist changes and return accepted response")
    void updateProjectById_WithValidRequest_ReturnsAcceptedProject() throws Exception {
        UpdateProjectRequestDto requestDto = new UpdateProjectRequestDto();
        requestDto.setName("Updated Project");
        requestDto.setDescription("Updated description");

        mockMvc.perform(put("/api/projects/{id}", EXISTING_PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(EXISTING_PROJECT_ID))
                .andExpect(jsonPath("$.name").value("Updated Project"))
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    @DisplayName("Delete project should return no content")
    void deleteProjectById_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/projects/{id}", EXISTING_PROJECT_ID))
                .andExpect(status().isNoContent());
    }
}
