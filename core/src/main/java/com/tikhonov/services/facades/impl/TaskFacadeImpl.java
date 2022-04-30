package com.tikhonov.services.facades.impl;

import com.tikhonov.exceptions.NotFoundException;
import com.tikhonov.models.Task;
import com.tikhonov.models.TaskStatus;
import com.tikhonov.models.User;
import com.tikhonov.models.dto.TaskRequestDto;
import com.tikhonov.models.dto.TaskResponseDto;
import com.tikhonov.models.dto.mappers.TaskMapper;
import com.tikhonov.services.TaskService;
import com.tikhonov.services.UserService;
import com.tikhonov.services.facades.TaskFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    public TaskResponseDto create(TaskRequestDto taskRequestDto) {
        var taskForSave = fillTask(taskRequestDto);
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

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public TaskResponseDto update(TaskRequestDto taskRequestDto) {
        var taskForUpdate = fillTask(taskRequestDto);
        return taskMapper.toResponseDto(taskService.update(taskForUpdate));
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

    private Task fillTask(TaskRequestDto taskRequestDto) {
        final Long assigneeId = taskRequestDto.getAssigneeId();
        var assignee = getUserById(assigneeId);

        var task = new Task();

        if (Objects.isNull(taskRequestDto.getId())) {
            task.setStatus(TaskStatus.NEW);
        } else {
            task.setStatus(taskRequestDto.getStatus());
        }

        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setAssignee(assignee);
        task.setPriority(taskRequestDto.getPriority());

        return task;
    }
}
