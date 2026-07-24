package com.example.taskmanagementapp.controller;

import com.example.taskmanagementapp.dto.user.UpdateUserProfileRequestDto;
import com.example.taskmanagementapp.dto.user.UpdateUserRoleRequestDto;
import com.example.taskmanagementapp.dto.user.UserResponseDto;
import com.example.taskmanagementapp.exception.UpdateUserProfileException;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for user management")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current user",
            description = "Returns information about the currently authenticated user"
    )
    public UserResponseDto getCurrentUser(@AuthenticationPrincipal User user) {
        return userService.getCurrentUserInfo(user);
    }

    @PatchMapping("/me")
    @Operation(summary = "Update user info",
            description = "Updates information about the currently authenticated user"
    )
    public UserResponseDto updateUserInfo(@AuthenticationPrincipal User user,
                                          @RequestBody @Valid UpdateUserProfileRequestDto requestDto)
            throws UpdateUserProfileException {
        return userService.updateUserInfo(user, requestDto);
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update user role",
            description = "Updates the role of a user with the specified ID"
    )
    public UserResponseDto updateUserRoleById(@PathVariable Long id,
                                              @RequestBody UpdateUserRoleRequestDto requestDto) {
        return userService.updateUserRoleById(id, requestDto);
    }
}
