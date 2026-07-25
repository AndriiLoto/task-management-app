package com.example.taskmanagementapp.service;

import com.example.taskmanagementapp.dto.label.CreateLabelRequestDto;
import com.example.taskmanagementapp.dto.label.LabelResponseDto;
import com.example.taskmanagementapp.dto.label.UpdateLabelRequestDto;
import com.example.taskmanagementapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LabelService {

    LabelResponseDto createLabel(User user, CreateLabelRequestDto requestDto);

    Page<LabelResponseDto> getAllLabels(User user, Pageable pageable);

    LabelResponseDto updateLabelById(Long id, User user, UpdateLabelRequestDto requestDto);

    void deleteLabelById(Long id, User user);
}
