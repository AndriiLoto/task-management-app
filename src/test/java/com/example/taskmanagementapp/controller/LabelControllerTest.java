package com.example.taskmanagementapp.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taskmanagementapp.dto.label.CreateLabelRequestDto;
import com.example.taskmanagementapp.dto.label.UpdateLabelRequestDto;
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
        scripts = {
                "classpath:database/add-users.sql",
                "classpath:database/add-labels.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = {
                "classpath:database/remove-labels.sql",
                "classpath:database/remove-users.sql"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
@WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class LabelControllerTest {
    private static final long EXISTING_LABEL_ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockitoBean
    private TelegramService telegramService;

    @Test
    @DisplayName("Create label should persist label and return created response")
    void createLabel_WithValidRequest_ReturnsCreatedLabel() throws Exception {
        CreateLabelRequestDto requestDto = new CreateLabelRequestDto();
        requestDto.setName("frontend");
        requestDto.setColor("#ff6633");

        mockMvc.perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("frontend"))
                .andExpect(jsonPath("$.color").value("#ff6633"));
    }

    @Test
    @DisplayName("Get all labels should return seeded labels page")
    void getAllLabels_ReturnsLabelPage() throws Exception {
        mockMvc.perform(get("/api/labels")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(EXISTING_LABEL_ID));
    }

    @Test
    @DisplayName("Update label should persist changes and return accepted response")
    void updateLabel_WithValidRequest_ReturnsAcceptedLabel() throws Exception {
        UpdateLabelRequestDto requestDto = new UpdateLabelRequestDto();
        requestDto.setName("backend-updated");
        requestDto.setColor("#000000");

        mockMvc.perform(put("/api/labels/{id}", EXISTING_LABEL_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(EXISTING_LABEL_ID))
                .andExpect(jsonPath("$.name").value("backend-updated"))
                .andExpect(jsonPath("$.color").value("#000000"));
    }

    @Test
    @DisplayName("Delete label should return no content")
    void deleteLabel_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/labels/{id}", EXISTING_LABEL_ID))
                .andExpect(status().isNoContent());
    }
}
