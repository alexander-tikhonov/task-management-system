package com.tikhonov.services;

import com.tikhonov.models.Task;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task create(Task task);

    Optional<Task> findById(Long id);

    List<Task> findAll();

    List<Task> findAllByAssigneeId(Long id, Sort sort);

    List<Task> findAllByCreatedById(Long id, Sort sort);

    Task update(Task task);

    void deleteById(Long id);
}
