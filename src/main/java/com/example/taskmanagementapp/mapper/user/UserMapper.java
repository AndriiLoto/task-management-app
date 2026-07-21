package com.example.taskmanagementapp.mapper.user;

import com.example.taskmanagementapp.config.MapperConfig;
import com.example.taskmanagementapp.dto.user.UpdateUserProfileRequestDto;
import com.example.taskmanagementapp.dto.user.UserRegistrationRequestDto;
import com.example.taskmanagementapp.dto.user.UserResponseDto;
import com.example.taskmanagementapp.model.Role;
import com.example.taskmanagementapp.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class,
        nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toUser(UserRegistrationRequestDto requestDto);

    UserResponseDto toUserResponseDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateUser(UpdateUserProfileRequestDto requestDto, @MappingTarget User user);

    default String mapRoleToString(Role role) {
        return role.getAuthority();
    }
}
