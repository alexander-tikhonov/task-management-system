package com.tikhonov.services.facades.impl;

import com.tikhonov.exceptions.NotFoundException;
import com.tikhonov.models.Task;
import com.tikhonov.models.TaskStatus;
import com.tikhonov.models.User;
import com.tikhonov.models.dto.TaskRequestDto;
import com.tikhonov.models.dto.TaskResponseDto;
import com.tikhonov.models.dto.mappers.TaskMapper;
import com.tikhonov.security.user.CustomUserDetails;
import com.tikhonov.services.TaskService;
import com.tikhonov.services.UserService;
import com.tikhonov.services.facades.TaskFacade;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskFacadeImpl implements TaskFacade {

    private final TaskService taskService;
    private final UserService userService;
    private final TaskMapper taskMapper;

    public TaskFacadeImpl(TaskService taskService, UserService userService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.userService = userService;
        this.taskMapper = taskMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public TaskResponseDto create(TaskRequestDto taskRequestDto, Authentication authentication) {
        final Long assigneeId = taskRequestDto.getAssigneeId();
        var assignee = getUserById(assigneeId);
        var createdBy = getAuthUser(authentication);

        var taskForSave = new Task();
        taskForSave.setTitle(taskRequestDto.getTitle());
        taskForSave.setDescription(taskRequestDto.getDescription());
        taskForSave.setCreatedBy(createdBy);
        taskForSave.setAssignee(assignee);
        taskForSave.setStatus(TaskStatus.NEW);
        taskForSave.setPriority(taskRequestDto.getPriority());

        return taskMapper.toResponseDto(taskService.create(taskForSave));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<TaskResponseDto> findById(Long id) {
        return taskService.findById(id).map(taskMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskResponseDto> findAll() {
        return taskService.findAll().stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskResponseDto> findAllForAssignee(Sort sort, Authentication authentication) {
        Long authId = getAuthUser(authentication).getId();

        return taskService.findAllByAssigneeId(authId, sort).stream()
                .map(taskMapper::toResponseDto).toList();
    }

    @Override
    public List<TaskResponseDto> findAllForCreatedBy(Sort sort, Authentication authentication) {
        Long authId = getAuthUser(authentication).getId();

        return taskService.findAllByCreatedById(authId, sort).stream()
                .map(taskMapper::toResponseDto).toList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public TaskResponseDto update(TaskRequestDto taskRequestDto) {
        final Long assigneeId = taskRequestDto.getAssigneeId();
        var assignee = getUserById(assigneeId);
        var task = getTaskById(taskRequestDto.getId());

        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setAssignee(assignee);
        task.setStatus(taskRequestDto.getStatus());
        task.setPriority(taskRequestDto.getPriority());

        return taskMapper.toResponseDto(taskService.update(task));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteById(Long id) {
        taskService.deleteById(id);
    }

    private User getUserById(Long id) {
        return userService.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("User with id: %d not found", id))
        );
    }

    private Task getTaskById(Long id) {
        return taskService.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Task with id: %d not found", id))
        );
    }

    private User getAuthUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
