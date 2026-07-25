package com.example.taskmanagementapp.dto.label;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateLabelRequestDto {
    @NotBlank(message = "Label name cannot be blank")
    private String name;
    @NotBlank(message = "Label color cannot be blank")
    private String color;
}
