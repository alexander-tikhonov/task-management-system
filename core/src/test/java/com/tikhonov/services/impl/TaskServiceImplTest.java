package com.tikhonov.services.impl;

import com.tikhonov.exceptions.TaskServiceException;
import com.tikhonov.models.Task;
import com.tikhonov.models.TaskStatus;
import com.tikhonov.models.User;
import com.tikhonov.repositories.TaskRepository;
import com.tikhonov.services.TaskService;
import com.tikhonov.validators.FieldValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName(" Service for working with tasks")
@SpringBootTest
class TaskServiceImplTest {

    private static final Long TASK_ID = 1L;
    private static final String TASK_TITLE = "Some title";
    private static final String TASK_DESCRIPTION = "Some description";

    private static final Long CREATED_BY_ID = 1L;
    private static final String CREATED_BY_NAME = "Vasya";
    private static final String CREATED_BY_EMAIL = "u1@mail.ru";

    private static final Long ASSIGNEE_ID = 2L;
    private static final String ASSIGNEE_NAME = "Ivan";
    private static final String ASSIGNEE_EMAIL = "u2@mail.ru";

    @Configuration
    @Import(TaskServiceImpl.class)
    static class TaskServiceImplTestConfig {}

    @Autowired
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private FieldValidator fieldValidator;

    private User createdByUser;
    private User assignee;

    @BeforeEach
    void setUp() {
        createdByUser = new User(CREATED_BY_ID, CREATED_BY_NAME, CREATED_BY_EMAIL);
        assignee = new User(ASSIGNEE_ID, ASSIGNEE_NAME, ASSIGNEE_EMAIL);
    }

    @DisplayName(" should throw exception when task has incorrect format")
    @Test
    public void shouldThrowExceptionWhenTaskHasIncorrectFormat() {
        var task = new Task(TASK_ID,null, TASK_DESCRIPTION,
                createdByUser, assignee, TaskStatus.NEW, Task.Priority.MEDIUM);

        given(fieldValidator.validate(task)).willReturn(false);

        assertThatThrownBy(() -> taskService.update(task))
                .isInstanceOf(TaskServiceException.class);
    }

    @DisplayName(" should correct save task")
    @Test
    public void shouldCorrectSaveTask() {
        var task = new Task(null, TASK_TITLE,TASK_DESCRIPTION,
                createdByUser, assignee, TaskStatus.NEW, Task.Priority.MEDIUM);

        var expectedTask = new Task(TASK_ID, TASK_TITLE,TASK_DESCRIPTION,
                createdByUser, assignee, TaskStatus.NEW, Task.Priority.MEDIUM);

        given(fieldValidator.validate(task)).willReturn(true);
        given(taskRepository.save(task)).willReturn(expectedTask);

        assertThat(taskService.create(task)).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedTask);
    }

    @DisplayName(" should find existing task by id")
    @Test
    public void shouldFindExistingTaskById() {
        var task = new Task(TASK_ID, TASK_TITLE,TASK_DESCRIPTION,
                createdByUser, assignee, TaskStatus.NEW, Task.Priority.MEDIUM);

        given(taskRepository.findById(TASK_ID)).willReturn(Optional.of(task));
        assertThat(taskService.findById(TASK_ID)).get().isEqualTo(task);
    }

    @DisplayName("should find all tasks")
    @Test
    public void shouldFindAllTasks() {
        List<Task> tasks = List.of(
                new Task(1L, TASK_TITLE,TASK_DESCRIPTION, createdByUser, assignee, TaskStatus.NEW, Task.Priority.MEDIUM),
                new Task(2L, TASK_TITLE,TASK_DESCRIPTION, createdByUser, assignee, TaskStatus.NEW, Task.Priority.MEDIUM),
                new Task(3L, TASK_TITLE,TASK_DESCRIPTION, createdByUser, assignee, TaskStatus.NEW, Task.Priority.MEDIUM)
        );

        given(taskRepository.findAll()).willReturn(tasks);
        assertThat(taskService.findAll()).isNotNull();
    }

    @DisplayName(" should correct update task")
    @Test
    public void shouldCorrectUpdateTask() {
        var task = new Task(TASK_ID, TASK_TITLE,TASK_DESCRIPTION,
                createdByUser, assignee, TaskStatus.NEW, Task.Priority.MEDIUM);

        given(fieldValidator.validate(task)).willReturn(true);
        given(taskRepository.save(task)).willReturn(task);

        assertThat(taskService.update(task)).isNotNull();
    }

    @DisplayName(" should correct delete existing task by id")
    @Test
    public void shouldCorrectDeleteExistingTaskById() {
        assertThatNoException().isThrownBy(() -> taskService.deleteById(TASK_ID));
    }
}