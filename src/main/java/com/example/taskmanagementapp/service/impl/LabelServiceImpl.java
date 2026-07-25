package com.example.taskmanagementapp.service.impl;

import com.example.taskmanagementapp.dto.label.CreateLabelRequestDto;
import com.example.taskmanagementapp.dto.label.LabelResponseDto;
import com.example.taskmanagementapp.dto.label.UpdateLabelRequestDto;
import com.example.taskmanagementapp.exception.EntityNotFoundException;
import com.example.taskmanagementapp.mapper.label.LabelMapper;
import com.example.taskmanagementapp.model.Label;
import com.example.taskmanagementapp.model.User;
import com.example.taskmanagementapp.repository.LabelRepository;
import com.example.taskmanagementapp.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    @Override
    public LabelResponseDto createLabel(User user, CreateLabelRequestDto requestDto) {
        Label label = labelMapper.toLabel(requestDto);
        labelRepository.save(label);
        return labelMapper.toLabelResponseDto(label);
    }

    @Override
    public Page<LabelResponseDto> getAllLabels(User user, Pageable pageable) {
        return labelRepository.findAll(pageable)
                .map(labelMapper::toLabelResponseDto);
    }

    @Override
    public LabelResponseDto updateLabelById(Long id, User user, UpdateLabelRequestDto requestDto) {
        Label label = labelRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Label not found with id " + id)
        );
        labelMapper.updateLabelFromDto(requestDto, label);
        Label updatedLabel = labelRepository.save(label);
        return labelMapper.toLabelResponseDto(updatedLabel);
    }

    @Override
    public void deleteLabelById(Long id, User user) {
        labelRepository.deleteById(id);
    }
}
