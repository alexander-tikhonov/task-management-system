package com.tikhonov.models.dto;

import com.tikhonov.models.TaskPriority;
import com.tikhonov.models.TaskStatus;
import com.tikhonov.models.User;

import java.io.Serial;
import java.io.Serializable;

public class TaskResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -5352809014592127874L;

    private Long id;
    private String title;
    private String description;
    private UserResponseDto createdBy;
    private UserResponseDto assignee;
    private TaskStatus status;
    private TaskPriority priority;

    public TaskResponseDto() {}

    public TaskResponseDto(Long id,
                           String title,
                           String description,
                           UserResponseDto createdBy,
                           UserResponseDto assignee,
                           TaskStatus status,
                           TaskPriority priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.assignee = assignee;
        this.status = status;
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserResponseDto getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserResponseDto createdBy) {
        this.createdBy = createdBy;
    }

    public UserResponseDto getAssignee() {
        return assignee;
    }

    public void setAssignee(UserResponseDto assignee) {
        this.assignee = assignee;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }
}
