package com.tikhonov.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikhonov.models.*;
import com.tikhonov.models.dto.CommentRequestDto;
import com.tikhonov.models.dto.CommentResponseDto;
import com.tikhonov.services.facades.CommentFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Controller for working with comments")
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    private static final Long USER_ID = 1L;
    private static final String USER_NAME = "User";
    private static final String USER_EMAIL = "u@mail.ru";
    private static final Long TASK_ID = 1L;
    private static final Long COMMENT_ID = 1L;
    private static final String COMMENT_CONTENT = "Some content";

    private static final String COMMENTS_API_URL = "/api/comments";
    private static final String COMMENTS_BY_TASK_ID = COMMENTS_API_URL + "?taskId={id}";
    private static final String COMMENT_BY_ID = COMMENTS_API_URL + "/{id}";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentFacade commentFacade;

    private CommentResponseDto expectedComment;

    @BeforeEach
    void setUp() {
        var user = new User(USER_ID, USER_NAME, USER_EMAIL);
        expectedComment = new CommentResponseDto(COMMENT_ID, COMMENT_CONTENT, LocalDateTime.now(), user);
    }

    @DisplayName(" should correct create comment")
    @Test
    public void shouldCorrectCreateComment() throws Exception {
        var commentForSave = new CommentRequestDto(null, COMMENT_CONTENT, USER_ID, TASK_ID);

        var requestBuilder = post(COMMENTS_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentForSave));

        given(commentFacade.create(any())).willReturn(expectedComment);

        mvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedComment)));
    }

    @DisplayName(" should find comments by task id")
    @Test
    public void shouldFindCommentsByTaskId() throws Exception {
        var requestBuilder = get(COMMENTS_BY_TASK_ID, TASK_ID)
                .contentType(MediaType.APPLICATION_JSON);


        List<CommentResponseDto> comments = Collections.singletonList(expectedComment);
        given(commentFacade.findAllByTaskId(TASK_ID)).willReturn(comments);

        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(comments)));
    }

    @DisplayName(" should correct delete comment by id")
    @Test
    public void shouldCorrectDeleteTaskById() throws Exception {
        var requestBuilder = delete(COMMENT_BY_ID, COMMENT_ID)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @DisplayName(" should return Bad Request status when sending incorrect data")
    @Test
    public void shouldReturnBadRequestStatusWhenSendingIncorrectData() throws Exception {
        var incorrectComment = new CommentRequestDto(null, "", USER_ID, TASK_ID);

        var requestBuilder = post(COMMENTS_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incorrectComment));

        mvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @DisplayName(" should return Bad Request status when sending incorrect param")
    @Test
    public void shouldReturnBadRequestStatusWhenSendingIncorrectParam() throws Exception {
        var requestBuilder = get(COMMENTS_BY_TASK_ID, 0)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }
}