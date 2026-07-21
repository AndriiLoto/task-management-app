package com.example.taskmanagementapp.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateUserProfileRequestDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
