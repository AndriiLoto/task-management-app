package com.example.taskmanagementapp.mapper.user;

import com.example.taskmanagementapp.config.MapperConfig;
import com.example.taskmanagementapp.dto.user.UserRegistrationRequestDto;
import com.example.taskmanagementapp.dto.user.UserResponseDto;
import com.example.taskmanagementapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(config = MapperConfig.class,
nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    User toUser(UserRegistrationRequestDto requestDto);

    UserResponseDto toUserResponseDto(User user);
}
