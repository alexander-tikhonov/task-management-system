package com.tikhonov.controllers.api;

import com.tikhonov.exceptions.NotFoundException;
import com.tikhonov.models.dto.TaskRequestDto;
import com.tikhonov.models.dto.TaskResponseDto;
import com.tikhonov.services.facades.TaskFacade;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@Validated
public class TaskController {
    private final TaskFacade taskFacade;

    public TaskController(TaskFacade taskFacade) {
        this.taskFacade = taskFacade;
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskResponseDto> create(@Valid @RequestBody TaskRequestDto task,
                                                  Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskFacade.create(task, authentication));
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

    @GetMapping("/tasks/assignee")
    public ResponseEntity<List<TaskResponseDto>> findAllForAssignee(
            @RequestParam(required = false) Sort.Direction statusDirection,
            @RequestParam(required = false) Sort.Direction priorityDirection,
            Authentication authentication) {

        var sort = getSort(statusDirection, priorityDirection);
        return ResponseEntity.ok(taskFacade.findAllForAssignee(sort, authentication));
    }

    @GetMapping("/tasks/createdBy")
    public ResponseEntity<List<TaskResponseDto>> findAllForCreatedBy(
            @RequestParam(required = false) Sort.Direction statusDirection,
            @RequestParam(required = false) Sort.Direction priorityDirection,
            Authentication authentication) {

        var sort = getSort(statusDirection, priorityDirection);
        return ResponseEntity.ok(taskFacade.findAllForCreatedBy(sort, authentication));
    }

    private Sort getSort(Sort.Direction statusDirection, Sort.Direction priorityDirection) {
        var sort = Sort.unsorted();
        if (!Objects.isNull(statusDirection)) {
            sort = sort.and(Sort.by(statusDirection, "status"));
        }
        if (!Objects.isNull(priorityDirection)) {
            sort = sort.and(Sort.by(priorityDirection, "priority"));
        }

        return sort;
    }
}

