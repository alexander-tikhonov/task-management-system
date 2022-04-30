package com.tikhonov.repositories;

import com.tikhonov.models.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @EntityGraph(value = "tasks-eg")
    @Override
    Optional<Task> findById(Long id);

    @EntityGraph(value = "tasks-eg")
    @Override
    List<Task> findAll();
}
