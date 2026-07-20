package com.example.taskmanagementapp.dto.user;

import com.example.taskmanagementapp.validation.fieldmatch.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@FieldMatch(field = "password", repeatedField = "repeatPassword")
public class UserRegistrationRequestDto {
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 6 characters long")
    private String password;
    @NotBlank(message = "Repeated password cannot be blank")
    private String repeatPassword;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not valid")
    private String email;
    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;
}
