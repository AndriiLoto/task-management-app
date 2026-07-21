package com.example.taskmanagementapp.service;

import com.example.taskmanagementapp.dto.user.UpdateUserProfileRequestDto;
import com.example.taskmanagementapp.dto.user.UserRegistrationRequestDto;
import com.example.taskmanagementapp.dto.user.UserResponseDto;
import com.example.taskmanagementapp.exception.RegistrationException;
import com.example.taskmanagementapp.exception.UpdateUserProfileException;
import com.example.taskmanagementapp.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

    UserResponseDto getCurrentUserInfo(User user);

    UserResponseDto updateUserInfo(User user, UpdateUserProfileRequestDto requestDto)
            throws UpdateUserProfileException;
}
