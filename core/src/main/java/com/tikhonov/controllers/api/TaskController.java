package com.tikhonov.controllers.api;

import com.tikhonov.exceptions.NotFoundException;
import com.tikhonov.models.Task;
import com.tikhonov.models.dto.TaskRequestDto;
import com.tikhonov.models.dto.TaskResponseDto;
import com.tikhonov.services.TaskService;
import com.tikhonov.services.facades.TaskFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class TaskController {

    private final TaskFacade taskFacade;

    public TaskController(TaskFacade taskFacade) {
        this.taskFacade = taskFacade;
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskResponseDto> create(@Valid @RequestBody TaskRequestDto task) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskFacade.create(task));
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskResponseDto> findById(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(taskFacade.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Task with id: %d not found!", id))
        ));
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponseDto>> findAll() {
        return ResponseEntity.ok(taskFacade.findAll());
    }

    @PutMapping("/tasks")
    public ResponseEntity<TaskResponseDto> update(@Valid @RequestBody TaskRequestDto task) {
        return ResponseEntity.ok(taskFacade.update(task));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> deleteById(@PathVariable @Min(1) Long id) {
        taskFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

