package com.example.taskmanagementapp.service.impl;

import com.example.taskmanagementapp.dto.user.UpdateUserProfileRequestDto;
import com.example.taskmanagementapp.dto.user.UserRegistrationRequestDto;
import com.example.taskmanagementapp.dto.user.UserResponseDto;
import com.example.taskmanagementapp.exception.EntityNotFoundException;
import com.example.taskmanagementapp.exception.RegistrationException;
import com.example.taskmanagementapp.exception.UpdateUserProfileException;
import com.example.taskmanagementapp.mapper.user.UserMapper;
import com.example.taskmanagementapp.model.Role;
import com.example.taskmanagementapp.model.RoleName;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.repository.RoleRepository;
import com.example.taskmanagementapp.repository.UserRepository;
import com.example.taskmanagementapp.service.UserService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByUsernameOrEmail(
                requestDto.getUsername(),
                requestDto.getEmail())) {
            throw new RegistrationException("Email or Username already in use "
                    + requestDto.getEmail()
                    + " "
                    + requestDto.getUsername());
        }
        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.USER).orElseThrow(
                () -> new EntityNotFoundException("User Role not found with name " + RoleName.USER)
        );
        user.getRoles().add(userRole);
        userRepository.save(user);
        return userMapper.toUserResponseDto(user);
    }

    @Override
    public UserResponseDto getCurrentUserInfo(User user) {
        return userMapper.toUserResponseDto(user);
    }

    @Override
    public UserResponseDto updateUserInfo(User user, UpdateUserProfileRequestDto requestDto)
            throws UpdateUserProfileException {
        User updatedUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("User not found with id " + user.getId())
        );
        validateUniqueProfileFields(updatedUser, requestDto);
        userMapper.updateUser(requestDto, updatedUser);
        updatedUser = userRepository.save(updatedUser);
        return userMapper.toUserResponseDto(updatedUser);
    }

    private void validateUniqueProfileFields(User user, UpdateUserProfileRequestDto requestDto)
            throws UpdateUserProfileException {
        if (requestDto.getUsername() != null
                && !Objects.equals(user.getUsername(), requestDto.getUsername())
                && userRepository.existsByUsernameAndIdNot(
                        requestDto.getUsername(), user.getId())) {
            throw new UpdateUserProfileException(
                    "Username already in use " + requestDto.getUsername());
        }
        if (requestDto.getEmail() != null
                && !Objects.equals(user.getEmail(), requestDto.getEmail())
                && userRepository.existsByEmailAndIdNot(requestDto.getEmail(), user.getId())) {
            throw new UpdateUserProfileException("Email already in use " + requestDto.getEmail());
        }
    }
}
