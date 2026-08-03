package com.example.taskmanagementapp.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taskmanagementapp.dto.user.UpdateUserProfileRequestDto;
import com.example.taskmanagementapp.dto.user.UpdateUserRoleRequestDto;
import com.example.taskmanagementapp.model.RoleName;
import com.example.taskmanagementapp.service.telegram.TelegramService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        scripts = "classpath:database/add-users.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = "classpath:database/remove-users.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
class UserControllerTest {
    private static final long ADMIN_ID = 1L;
    private static final long ASSIGNEE_ID = 2L;

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockitoBean
    private TelegramService telegramService;

    @Test
    @WithUserDetails(value = "assignee", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Get current user should return authenticated user")
    void getCurrentUser_ReturnsCurrentUser() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ASSIGNEE_ID))
                .andExpect(jsonPath("$.username").value("assignee"))
                .andExpect(jsonPath("$.email").value("assignee@example.com"))
                .andExpect(jsonPath("$.roles", hasItem("USER")));
    }

    @Test
    @WithUserDetails(value = "assignee", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Update current user profile should persist changes")
    void updateUserInfo_WithValidRequest_ReturnsUpdatedUser() throws Exception {
        UpdateUserProfileRequestDto requestDto = new UpdateUserProfileRequestDto();
        requestDto.setUsername("assignee-updated");
        requestDto.setEmail("assignee.updated@example.com");
        requestDto.setFirstName("Updated");
        requestDto.setLastName("Assignee");

        mockMvc.perform(patch("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ASSIGNEE_ID))
                .andExpect(jsonPath("$.username").value("assignee-updated"))
                .andExpect(jsonPath("$.email").value("assignee.updated@example.com"))
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("Assignee"));
    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Update user role should persist new role")
    void updateUserRoleById_WithValidRequest_ReturnsUser() throws Exception {
        UpdateUserRoleRequestDto requestDto = new UpdateUserRoleRequestDto();
        requestDto.setRole(RoleName.ADMIN);

        mockMvc.perform(put("/api/users/{id}/role", ASSIGNEE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ASSIGNEE_ID))
                .andExpect(jsonPath("$.roles", hasItem("ADMIN")));
    }

    @Test
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Create Telegram link token should return generated token")
    void createTelegramLinkToken_ReturnsToken() throws Exception {
        mockMvc.perform(post("/api/users/me/telegram-link"))
                .andExpect(status().isOk())
                .andExpect(content().string(matchesPattern("[A-Za-z0-9]{7}")));

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ADMIN_ID));
    }
}
