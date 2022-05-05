package com.tikhonov.services.facades;

import com.tikhonov.models.dto.TaskRequestDto;
import com.tikhonov.models.dto.TaskResponseDto;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface TaskFacade {

    TaskResponseDto create(TaskRequestDto taskRequestDto, Authentication authentication);

    Optional<TaskResponseDto> findById(Long id);

    List<TaskResponseDto> findAll();

    List<TaskResponseDto> findAllForAssignee(Sort sort, Authentication authentication);

    List<TaskResponseDto> findAllForCreatedBy(Sort sort, Authentication authentication);

    TaskResponseDto update(TaskRequestDto taskRequestDto);

    void deleteById(Long id);
}
