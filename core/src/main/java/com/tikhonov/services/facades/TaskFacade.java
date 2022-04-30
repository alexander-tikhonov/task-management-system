package com.tikhonov.services.facades;

import com.tikhonov.models.dto.TaskRequestDto;
import com.tikhonov.models.dto.TaskResponseDto;

import java.util.List;
import java.util.Optional;

public interface TaskFacade {

    TaskResponseDto create(TaskRequestDto taskRequestDto);

    Optional<TaskResponseDto> findById(Long id);

    List<TaskResponseDto> findAll();

    TaskResponseDto update(TaskRequestDto taskRequestDto);

    void deleteById(Long id);
}
