package com.example.taskmanagementapp.controller;

import com.example.taskmanagementapp.dto.user.UpdateUserProfileRequestDto;
import com.example.taskmanagementapp.dto.user.UserResponseDto;
import com.example.taskmanagementapp.exception.UpdateUserProfileException;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public UserResponseDto updateUserInfo(@AuthenticationPrincipal User user,
                                          @RequestBody UpdateUserProfileRequestDto requestDto)
            throws UpdateUserProfileException {
        return userService.updateUserInfo(user, requestDto);
    }
}
