package com.tikhonov.models.dto;

import com.tikhonov.models.User;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


public class CommentResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 8050929846491972223L;

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private User user;

    public CommentResponseDto() {}

    public CommentResponseDto(Long id, String content, LocalDateTime createdAt, User user) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
