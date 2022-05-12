package com.tikhonov.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serial;
import java.io.Serializable;

public class CommentRequestDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -1412767723712803223L;

    private Long id;

    @NotBlank
    private String content;

    @NotNull
    @Positive
    private Long taskId;

    public CommentRequestDto() {}

    public CommentRequestDto(Long id, String content, Long taskId) {
        this.id = id;
        this.content = content;
        this.taskId = taskId;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getTaskId() {
        return taskId;
    }
}
