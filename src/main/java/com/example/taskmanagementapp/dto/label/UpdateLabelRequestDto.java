package com.example.taskmanagementapp.dto.label;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateLabelRequestDto {
    private String name;
    private String color;
}
