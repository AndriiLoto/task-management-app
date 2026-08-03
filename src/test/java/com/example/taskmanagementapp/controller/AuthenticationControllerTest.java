package com.example.taskmanagementapp.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taskmanagementapp.dto.user.UserLoginRequestDto;
import com.example.taskmanagementapp.dto.user.UserRegistrationRequestDto;
import com.example.taskmanagementapp.service.telegram.TelegramService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockitoBean
    private TelegramService telegramService;

    @Test
    @Sql(
            scripts = "classpath:database/remove-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Register should persist new user and return user response")
    void register_WithValidRequest_ReturnsUserResponse() throws Exception {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setUsername("new-user");
        requestDto.setPassword("password123");
        requestDto.setRepeatPassword("password123");
        requestDto.setEmail("new-user@example.com");
        requestDto.setFirstName("New");
        requestDto.setLastName("User");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value("new-user"))
                .andExpect(jsonPath("$.email").value("new-user@example.com"))
                .andExpect(jsonPath("$.roles", hasItem("USER")));
    }

    @Test
    @Sql(
            scripts = "classpath:database/add-users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/remove-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Login should authenticate seeded user and return JWT")
    void login_WithValidCredentials_ReturnsToken() throws Exception {
        UserLoginRequestDto requestDto = new UserLoginRequestDto();
        requestDto.setUsername("admin");
        requestDto.setPassword("password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(blankOrNullString())));
    }
}
