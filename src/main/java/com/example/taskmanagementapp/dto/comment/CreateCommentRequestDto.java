package com.example.taskmanagementapp.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateCommentRequestDto {
    @NotNull(message = "Comment text cannot be blank")
    private Long taskId;
    @NotBlank(message = "Comment text cannot be blank")
    private String text;
}
