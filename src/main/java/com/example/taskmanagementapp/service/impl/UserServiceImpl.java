package com.example.taskmanagementapp.service.impl;

import com.example.taskmanagementapp.dto.user.UserRegistrationRequestDto;
import com.example.taskmanagementapp.dto.user.UserResponseDto;
import com.example.taskmanagementapp.exception.EntityNotFoundException;
import com.example.taskmanagementapp.exception.RegistrationException;
import com.example.taskmanagementapp.mapper.user.UserMapper;
import com.example.taskmanagementapp.model.Role;
import com.example.taskmanagementapp.model.RoleName;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.repository.RoleRepository;
import com.example.taskmanagementapp.repository.UserRepository;
import com.example.taskmanagementapp.service.UserService;
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
}
