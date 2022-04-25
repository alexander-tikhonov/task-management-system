package com.tikhonov.services;

import com.tikhonov.models.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task create(Task task);

    Optional<Task> findById(Long id);

    List<Task> findAll();

    Task update(Task task);

    void deleteById(Long id);
}
