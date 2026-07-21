package com.example.taskmanagementapp.dto.user;

import com.example.taskmanagementapp.model.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateUserRoleRequestDto {
    @NotNull(message = "Role cannot be blank")
    private RoleName role;
}
