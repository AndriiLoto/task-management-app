package com.example.taskmanagementapp.service;

import static com.example.taskmanagementapp.util.TestDataFactory.role;
import static com.example.taskmanagementapp.util.TestDataFactory.user;
import static com.example.taskmanagementapp.util.TestDataFactory.userResponseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.taskmanagementapp.dto.user.UpdateUserProfileRequestDto;
import com.example.taskmanagementapp.dto.user.UpdateUserRoleRequestDto;
import com.example.taskmanagementapp.dto.user.UserRegistrationRequestDto;
import com.example.taskmanagementapp.dto.user.UserResponseDto;
import com.example.taskmanagementapp.exception.RegistrationException;
import com.example.taskmanagementapp.exception.UpdateUserProfileException;
import com.example.taskmanagementapp.mapper.user.UserMapper;
import com.example.taskmanagementapp.model.Role;
import com.example.taskmanagementapp.model.RoleName;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.repository.RoleRepository;
import com.example.taskmanagementapp.repository.UserRepository;
import com.example.taskmanagementapp.service.impl.UserServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Register should encode password, assign USER role, save user and return dto")
    void register_WithUniqueUsernameAndEmail_ReturnsUserResponseDto() throws RegistrationException {
        UserRegistrationRequestDto requestDto = registrationRequest();
        User mappedUser = user(1L);
        mappedUser.getRoles().clear();
        Role userRole = role(RoleName.USER);
        UserResponseDto expected = userResponseDto(1L);

        when(userRepository.existsByUsernameOrEmail(requestDto.getUsername(), requestDto.getEmail()))
                .thenReturn(false);
        when(userMapper.toUser(requestDto)).thenReturn(mappedUser);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encoded-password");
        when(roleRepository.findByName(RoleName.USER)).thenReturn(Optional.of(userRole));
        when(userMapper.toUserResponseDto(mappedUser)).thenReturn(expected);

        UserResponseDto actual = userService.register(requestDto);

        assertThat(actual).isEqualTo(expected);
        assertThat(mappedUser.getPassword()).isEqualTo("encoded-password");
        assertThat(mappedUser.getRoles()).containsExactly(userRole);
        verify(userRepository).save(mappedUser);
    }

    @Test
    @DisplayName("Register should throw RegistrationException when username or email exists")
    void register_WithExistingUsernameOrEmail_ThrowsRegistrationException() {
        UserRegistrationRequestDto requestDto = registrationRequest();
        when(userRepository.existsByUsernameOrEmail(requestDto.getUsername(), requestDto.getEmail()))
                .thenReturn(true);

        assertThatThrownBy(() -> userService.register(requestDto))
                .isInstanceOf(RegistrationException.class)
                .hasMessageContaining(requestDto.getEmail())
                .hasMessageContaining(requestDto.getUsername());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Update user info should reject username used by another account")
    void updateUserInfo_WithDuplicateUsername_ThrowsUpdateUserProfileException() {
        User currentUser = user(1L);
        UpdateUserProfileRequestDto requestDto = new UpdateUserProfileRequestDto();
        requestDto.setUsername("existing");

        when(userRepository.findById(currentUser.getId())).thenReturn(Optional.of(currentUser));
        when(userRepository.existsByUsernameAndIdNot(requestDto.getUsername(), currentUser.getId()))
                .thenReturn(true);

        assertThatThrownBy(() -> userService.updateUserInfo(currentUser, requestDto))
                .isInstanceOf(UpdateUserProfileException.class)
                .hasMessageContaining("Username already in use");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Update user role should replace existing roles and return updated user")
    void updateUserRoleById_WithExistingUserAndRole_ReplacesRole() {
        User existingUser = user(1L);
        Role adminRole = role(RoleName.ADMIN);
        UpdateUserRoleRequestDto requestDto = new UpdateUserRoleRequestDto();
        requestDto.setRole(RoleName.ADMIN);
        UserResponseDto expected = userResponseDto(1L);

        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        when(roleRepository.findByName(RoleName.ADMIN)).thenReturn(Optional.of(adminRole));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.toUserResponseDto(existingUser)).thenReturn(expected);

        UserResponseDto actual = userService.updateUserRoleById(existingUser.getId(), requestDto);

        assertThat(actual).isEqualTo(expected);
        assertThat(existingUser.getRoles()).containsExactly(adminRole);
        verify(userRepository).save(existingUser);
    }

    private UserRegistrationRequestDto registrationRequest() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setUsername("john");
        requestDto.setPassword("password123");
        requestDto.setRepeatPassword("password123");
        requestDto.setEmail("john@example.com");
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        return requestDto;
    }
}
