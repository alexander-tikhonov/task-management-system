package com.tikhonov.controllers.api;

import com.google.gson.Gson;
import com.tikhonov.models.TaskPriority;
import com.tikhonov.models.TaskStatus;
import com.tikhonov.models.User;
import com.tikhonov.models.dto.TaskRequestDto;
import com.tikhonov.models.dto.TaskResponseDto;
import com.tikhonov.services.facades.TaskFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Controller for working with tasks")
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    private static final Long TASK_ID = 1L;
    private static final String TASK_TITLE = "Task title";
    private static final String TASK_DESCRIPTION = "Task description";
    public static final Long ASSIGNEE_ID = 1L;
    public static final Long CREATED_BY_ID = 3L;
    private static final String TASKS_API_URL = "/api/tasks/";
    private static final String TASK_BY_ID_URL = TASKS_API_URL + "{id}";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TaskFacade taskFacade;

    private Gson gson;
    private TaskResponseDto expectedTask;

    @BeforeEach
    void setUp() {
        gson = new Gson();
        var assignee = new User();
        assignee.setId(ASSIGNEE_ID);

        var createdBy = new User();
        createdBy.setId(CREATED_BY_ID);

        expectedTask = new TaskResponseDto(TASK_ID, TASK_TITLE, TASK_DESCRIPTION,
                createdBy, assignee, TaskStatus.NEW, TaskPriority.MEDIUM);
    }

    @DisplayName(" should correct create task")
    @Test
    public void shouldCorrectCreateTask() throws Exception {
        var taskForSave = new TaskRequestDto(null, TASK_TITLE, TASK_DESCRIPTION,
                ASSIGNEE_ID, TaskStatus.NEW, TaskPriority.MEDIUM);


        var requestBuilder = post(TASKS_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(taskForSave));

        given(taskFacade.create(any())).willReturn(expectedTask);
        mvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(gson.toJson(expectedTask)));
    }

    @DisplayName(" should find existing task by id")
    @Test
    public void shouldFindExistingTaskById() throws Exception {
        var requestBuilder = get(TASK_BY_ID_URL, TASK_ID)
                .contentType(MediaType.APPLICATION_JSON);

        given(taskFacade.findById(TASK_ID)).willReturn(Optional.of(expectedTask));
        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(gson.toJson(expectedTask)));
    }

    @DisplayName(" should find all tasks")
    @Test
    public void shouldFindAllTasks() throws Exception {
        var requestBuilder = get(TASKS_API_URL)
                .contentType(MediaType.APPLICATION_JSON);

        List<TaskResponseDto> taskList = Collections.singletonList(expectedTask);
        given(taskFacade.findAll()).willReturn(taskList);

        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(gson.toJson(taskList)));
    }

    @DisplayName(" should correct update task")
    @Test
    public void shouldCorrectUpdateTask() throws Exception {
        var taskForUpdate = new TaskRequestDto(TASK_ID, TASK_TITLE, TASK_DESCRIPTION,
                ASSIGNEE_ID, TaskStatus.NEW, TaskPriority.MEDIUM);

        var requestBuilder = put(TASKS_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(taskForUpdate));

        given(taskFacade.update(any())).willReturn(expectedTask);

        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(gson.toJson(expectedTask)));
    }

    @DisplayName(" should correct delete task by id")
    @Test
    public void shouldCorrectDeleteTaskById() throws Exception {
        var requestBuilder = delete(TASK_BY_ID_URL, TASK_ID)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @DisplayName(" should return Not Found status when task is not found")
    @Test
    public void shouldReturnNotFoundStatusWhenTaskIsNotFound() throws Exception {
        var requestBuilder = get(TASK_BY_ID_URL, TASK_ID)
                .contentType(MediaType.APPLICATION_JSON);

        given(taskFacade.findById(TASK_ID)).willReturn(Optional.empty());

        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @DisplayName(" should return Bad Request status when sending incorrect data")
    @Test
    public void shouldReturnBadRequestStatusWhenSendingIncorrectData() throws Exception {
        var incorrectTask = new TaskRequestDto(TASK_ID, null, TASK_DESCRIPTION,
                ASSIGNEE_ID, TaskStatus.NEW, TaskPriority.MEDIUM);

        var requestBuilder = post(TASKS_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(incorrectTask));

        mvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @DisplayName(" should return Bad Request status when sending incorrect param")
    @Test
    public void shouldReturnBadRequestStatusWhenSendingIncorrectParam() throws Exception {
        var requestBuilder = get(TASK_BY_ID_URL, 0)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }
}