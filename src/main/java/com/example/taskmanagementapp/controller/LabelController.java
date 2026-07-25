package com.example.taskmanagementapp.controller;

import com.example.taskmanagementapp.dto.label.CreateLabelRequestDto;
import com.example.taskmanagementapp.dto.label.LabelResponseDto;
import com.example.taskmanagementapp.dto.label.UpdateLabelRequestDto;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/labels")
@Tag(name = "Label", description = "Endpoints for label management")
public class LabelController {
    private final LabelService labelService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new label",
            description = "Creates a new label for the authenticated user")
    public LabelResponseDto createLabel(@AuthenticationPrincipal User user,
                                        @RequestBody @Valid CreateLabelRequestDto requestDto) {
        return labelService.createLabel(user,requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all labels",
            description = "Get all labels for the authenticated user")
    public Page<LabelResponseDto> getAllLabels(@AuthenticationPrincipal User user,
                                               Pageable pageable) {
        return labelService.getAllLabels(user, pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Update Label", description = "Update Label by id")
    public LabelResponseDto updateLabel(@AuthenticationPrincipal User user,
                                        @PathVariable Long id,
                                        @RequestBody @Valid UpdateLabelRequestDto requestDto
    ) {
        return labelService.updateLabelById(id, user, requestDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Label", description = "Delete Label by id")
    public void deleteLabel(@AuthenticationPrincipal User user, @PathVariable Long id) {
        labelService.deleteLabelById(id, user);
    }
}
