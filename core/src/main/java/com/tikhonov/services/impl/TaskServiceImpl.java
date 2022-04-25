package com.tikhonov.services.impl;

import com.tikhonov.exceptions.TaskServiceException;
import com.tikhonov.models.Task;
import com.tikhonov.repositories.TaskRepository;
import com.tikhonov.services.TaskService;
import com.tikhonov.validators.FieldValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final FieldValidator fieldValidator;

    public TaskServiceImpl(TaskRepository taskRepository, FieldValidator fieldValidator) {
        this.taskRepository = taskRepository;
        this.fieldValidator = fieldValidator;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Task create(Task task) {
        checkTask(task);
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Task update(Task task) {
        checkTask(task);
        return taskRepository.save(task);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    private void checkTask(Task task) {
        if (!fieldValidator.validate(task)) {
            throw new TaskServiceException("Incorrect task format!");
        }
    }
}
