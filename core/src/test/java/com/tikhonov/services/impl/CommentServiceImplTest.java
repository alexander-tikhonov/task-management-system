package com.tikhonov.services.impl;

import com.tikhonov.exceptions.CommentServiceException;
import com.tikhonov.models.Comment;
import com.tikhonov.models.Task;
import com.tikhonov.models.TaskStatus;
import com.tikhonov.models.User;
import com.tikhonov.repositories.CommentRepository;
import com.tikhonov.services.CommentService;
import com.tikhonov.validators.FieldValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("Service for working with comments")
@SpringBootTest
class CommentServiceImplTest {

    private static final Long COMMENT_ID = 6L;
    private static final String COMMENT_CONTENT = "Some text";

    private static final Long CREATED_BY_ID = 5L;
    private static final String CREATED_BY_NAME = "CreatedBy";
    private static final String CREATED_BY_EMAIL = "user1@mail.ru";

    private static final Long ASSIGNEE_ID = 6L;
    private static final String ASSIGNEE_NAME = "Assignee";
    private static final String ASSIGNEE_EMAIL = "user2@mail.ru";

    private static final Long TASK_ID = 10L;
    private static final String TASK_TITLE = "Title";
    private static final String TASK_DESCRIPTION = "Description";

    @Configuration
    @Import(CommentServiceImpl.class)
    static class CommentServiceImplTestConfig {}

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private FieldValidator fieldValidator;

    private Task task;
    private User createdBy;
    private User assignee;

    @BeforeEach
    void setUp() {
        createdBy = new User(CREATED_BY_ID, CREATED_BY_NAME, CREATED_BY_EMAIL);
        assignee = new User(ASSIGNEE_ID, ASSIGNEE_NAME, ASSIGNEE_EMAIL);
        task = new Task(TASK_ID, TASK_TITLE, TASK_DESCRIPTION,
                createdBy, assignee, TaskStatus.NEW, Task.Priority.MEDIUM);
    }

    @DisplayName(" should throw exception when comment has incorrect format")
    @Test
    public void shouldThrowExceptionWhenCommentHasIncorrectFormat() {
        var comment = new Comment(COMMENT_ID, null, new Date(), task, createdBy);

        given(fieldValidator.validate(comment)).willReturn(false);
        assertThatThrownBy(() -> commentService.create(comment))
                .isInstanceOf(CommentServiceException.class);
    }

    @DisplayName(" should correct save comment")
    @Test
    public void shouldCorrectSaveComment() {
        var comment = new Comment(null, COMMENT_CONTENT, new Date(), task, createdBy);
        var expectedComment = new Comment(COMMENT_ID, COMMENT_CONTENT, new Date(), task, createdBy);

        given(fieldValidator.validate(comment)).willReturn(true);
        given(commentRepository.save(comment)).willReturn(expectedComment);

        assertThat(commentService.create(comment)).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedComment);
    }

    @DisplayName(" should find all comments by task id")
    @Test
    public void shouldFindAllCommentsByTaskId() {
        var comment = new Comment(COMMENT_ID, COMMENT_CONTENT, new Date(), task, createdBy);

        given(commentRepository.findAllByTask_Id(TASK_ID)).willReturn(List.of(comment));
        assertThat(commentService.findAllByTaskId(TASK_ID)).isNotNull();
    }

    @DisplayName(" should correct delete comment by id")
    @Test
    public void shouldCorrectDeleteCommentById() {
        assertThatNoException().isThrownBy(() -> commentService.deleteById(COMMENT_ID));
    }
}