package com.example.taskmanagementapp.controller;

import com.example.taskmanagementapp.dto.user.UserLoginRequestDto;
import com.example.taskmanagementapp.dto.user.UserLoginResponseDto;
import com.example.taskmanagementapp.dto.user.UserRegistrationRequestDto;
import com.example.taskmanagementapp.dto.user.UserResponseDto;
import com.example.taskmanagementapp.exception.RegistrationException;
import com.example.taskmanagementapp.security.AuthenticationService;
import com.example.taskmanagementapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Endpoints for authentication")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user",
            description = "Registers a new user with the provided username, password, and email")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
